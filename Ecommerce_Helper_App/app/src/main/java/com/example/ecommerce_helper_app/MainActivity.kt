package com.example.ecommerce_helper_app

import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.ecommerce_helper_app.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedImage = mutableListOf<Uri>()
    private var selectedColors = mutableListOf<Int>()
    private var fireBaseStorage = Firebase.storage.reference
    private var fireStore = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonColorPicker.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Product Color")
                .setPositiveButton("select", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        envelope?.let {
                            selectedColors.add(it.color)
                            updateColors()
                        }
                    }
                }).setNegativeButton("cancel") { colorPicker, _ ->
                    colorPicker.dismiss()

                }.show()
        }

        val selectImagesActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val intent = it.data

                    //multiple images

                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 0
                        (0 until count).forEach {
                            val imageuri = intent.clipData?.getItemAt(it)?.uri
                            imageuri?.let {
                                selectedImage.add(it)
                            }
                        }
                    } else {
                        val imageUri = intent?.data
                        imageUri?.let {
                            selectedImage.add(it)
                        }
                    }
                    updataImages()
                }

            }


        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }


    }

    private fun updataImages() {
        binding.tvSelectedImages.text = selectedImage.size.toString()
    }

    private fun updateColors() {
        var colors = ""
        selectedColors.forEach {
            colors = "$colors , ${Integer.toHexString(it)}"
        }
        binding.tvSelectedColors.text = colors
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //1
        if (item.itemId == R.id.saveProduct) {
            val productValidation = validateInformation()
            if (!productValidation) {
                Toast.makeText(this, "Check your inputs", Toast.LENGTH_LONG).show()
                return false
            }
            saveProducts()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveProducts() {
        val name = binding.edName.text.toString().trim()
        val category = binding.edCategory.text.toString().trim()
        val price = binding.edPrice.text.toString().trim()
        val offerPercentage = binding.edOfferPercentage.text.toString().trim()
        val description = binding.edDescription.text.toString().trim()
        val sizes = getSizesList(binding.edSizes.text.toString().trim())
        val imagesByteArrays = getImagesByteArrays()
        val images = mutableListOf<String>()


        lifecycleScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {
                showLoading()
            }

            try {

                async {
                    imagesByteArrays.forEach {
                        val id = UUID.randomUUID().toString()
                        launch {
                            val image_path = fireBaseStorage.child("products/images $id")
                            val result = image_path.putBytes(it).await()
                            val downloadUrl = result.storage.downloadUrl.await().toString()
                            images.add(downloadUrl)
                        }
                    }
                }.await()

            } catch (e: java.lang.Exception) {
                withContext(Dispatchers.Main) {
                    hideLoading()
                }
                e.printStackTrace()
            }

            val product = Product(
                UUID.randomUUID().toString(),
                name,
                category,
                price.toFloat(),
                if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                if (description.isEmpty()) null else description,
                if (selectedColors.isEmpty()) null else selectedColors,
                sizes,
                images

            )

            fireStore.collection("Product").add(product).addOnSuccessListener {
                hideLoading()

            }.addOnFailureListener {
                hideLoading()
                Log.e("Error", it.message.toString())
            }
        }

    }

    private fun hideLoading() {
        binding.progressbar.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        binding.progressbar.visibility = View.VISIBLE
    }

    private fun getImagesByteArrays(): List<ByteArray> {
        val imagesByteArray = mutableListOf<ByteArray>()
        selectedImage.forEach {
            val stream = ByteArrayOutputStream()
            val bitMap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            if (bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                imagesByteArray.add(stream.toByteArray())
            }
        }
        return imagesByteArray
    }

    private fun getSizesList(size: String): List<String>? {
        if (size.isEmpty()) {
            return null
        }
        val sizeList = size.split(",")
        return sizeList

    }


    private fun validateInformation(): Boolean {
        if (binding.edPrice.text.toString().trim().isEmpty())
            return false
        if (binding.edName.text.toString().trim().isEmpty())
            return false
        if (binding.edCategory.text.toString().trim().isEmpty())
            return false

        if (selectedImage.isEmpty())
            return false

        return true

    }
}

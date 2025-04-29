package pnj.pk.pareaipk.ui.scan

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pnj.pk.pareaipk.R
import pnj.pk.pareaipk.databinding.ActivityHasilScanBinding
import pnj.pk.pareaipk.utils.uriToFile

class HasilScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilScanBinding
    private val viewModel: HasilScanViewModel by viewModels()

    companion object {
        const val KEY_IMG_URI = "img_uri_key"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }

        val imageUriString = intent.getStringExtra(KEY_IMG_URI)
        if (imageUriString == null) {
            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val imageUri = Uri.parse(imageUriString)
        val imageFile = uriToFile(imageUri, this)
        binding.resultImage.setImageURI(imageUri)

        binding.progressBar.visibility = View.VISIBLE
        binding.resultContainer.visibility = View.GONE

        viewModel.predictImage(imageFile)

        viewModel.predictionResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            binding.resultContainer.visibility = View.VISIBLE

            result.onSuccess { mlResponse ->
                // Tampilkan hasil klasifikasi
                binding.resultTitle.text = mlResponse.classLabel
                binding.confidenceText.text = "Confidence: %.2f%%".format(mlResponse.confidence * 100)

                // Tampilkan suggestion dari server
                binding.resultDescription.text = "No explanation provided by server."
                binding.suggestionText.text = mlResponse.suggestion.ifBlank { "No suggestion provided." }
                binding.medicineText.text = "No medicine."

                // Tampilkan createdAt dari API
                binding.createdAt.text = "Created at: ${mlResponse.createdAt}"
            }

            result.onFailure { exception ->
                Toast.makeText(this, "Prediction failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.resultTitle.text = "Prediction Error"
                binding.resultDescription.text = exception.message ?: "Unknown error occurred"
            }
        }
    }
}

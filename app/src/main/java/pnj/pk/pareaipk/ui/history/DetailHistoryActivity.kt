package pnj.pk.pareaipk.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import pnj.pk.pareaipk.R
import pnj.pk.pareaipk.databinding.ActivityDetailHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private val detailViewModel: DetailHistoryViewModel by viewModels()

    companion object {
        const val EXTRA_SCAN_HISTORY_ID = "extra_scan_history_id"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide ActionBar
        supportActionBar?.hide()

        // Setup back navigation
        binding.topAppBar.setNavigationOnClickListener { onBackPressed() }

        // Get scan history ID from intent
        val extraScanHistoryId = intent.getIntExtra("extra_scan_history_id", -1).toLong()

        // Observe scan history details
        detailViewModel.loadScanHistoryById(extraScanHistoryId)
        detailViewModel.scanHistoryItem.observe(this, Observer { scanHistory ->
            scanHistory?.let {
                // Populate UI with scan history details
                binding.historyTitle.text = scanHistory.result
                binding.confidenceText.text = "Confidence: ${scanHistory.confidenceScore}%"
                binding.historyDescription.text = scanHistory.explanation
                binding.suggestionText.text = scanHistory.suggestion
                binding.medicineText.text = scanHistory.medicine

                // Format and set created at date
                val inputDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()) // Adjust to match input format
                val outputDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()) // Adjust to desired output format

                val date = inputDateFormat.parse(scanHistory.scanDate) // Parse the input date
                binding.createdAt.text = "Created at: ${outputDateFormat.format(date)}" // Format the parsed date

                // Load image using Glide
                Glide.with(this)
                    .load(scanHistory.imageUri)
                    .into(binding.historyImage)
            }
        })
    }
}
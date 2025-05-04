package pnj.pk.pareaipk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pnj.pk.pareaipk.adapter.HistoryHorizontalAdapter
import pnj.pk.pareaipk.databinding.FragmentHomeBinding
import pnj.pk.pareaipk.ui.chatbot.ChatbotActivity
import pnj.pk.pareaipk.ui.history.DetailHistoryActivity
import pnj.pk.pareaipk.ui.history.HistoryViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyHorizontalAdapter: HistoryHorizontalAdapter

    // Gunakan ViewModel dari package history
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // Siapkan adapter kosong
        historyHorizontalAdapter = HistoryHorizontalAdapter(emptyList()) { scanHistory ->
            // Setiap kali item di-klik, navigasikan ke DetailHistoryActivity
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                putExtra(DetailHistoryActivity.EXTRA_SCAN_HISTORY_ID, scanHistory.id) // Kirimkan ID riwayat
            }
            startActivity(intent)
        }

        binding.recyclerViewHistoryHorizontal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyHorizontalAdapter
        }

        // Observe LiveData dari ViewModel
        historyViewModel.allScanHistory.observe(viewLifecycleOwner) { historyList ->
            // Update adapter dengan data yang diterima dari ViewModel
            historyHorizontalAdapter = HistoryHorizontalAdapter(historyList) { scanHistory ->
                // Navigasikan ke DetailHistoryActivity ketika item di-klik
                val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                    putExtra(DetailHistoryActivity.EXTRA_SCAN_HISTORY_ID, scanHistory.id)
                }
                startActivity(intent)
            }
            binding.recyclerViewHistoryHorizontal.adapter = historyHorizontalAdapter
        }

        // Set up click listener for the FloatingActionButton (FAB)
        binding.buttonChat.setOnClickListener {
            // Start the ChatbotActivity when FAB is clicked
            val intent = Intent(requireContext(), ChatbotActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

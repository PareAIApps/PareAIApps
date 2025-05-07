package pnj.pk.pareaipk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pnj.pk.pareaipk.R
import pnj.pk.pareaipk.adapter.HistoryHorizontalAdapter
import pnj.pk.pareaipk.databinding.FragmentHomeBinding
import pnj.pk.pareaipk.ui.chatbot.ChatbotActivity
import pnj.pk.pareaipk.ui.history.DetailHistoryActivity
import pnj.pk.pareaipk.ui.history.HistoryViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyHorizontalAdapter: HistoryHorizontalAdapter
    private val historyViewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // Inisialisasi adapter kosong
        historyHorizontalAdapter = HistoryHorizontalAdapter(emptyList()) { scanHistory ->
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                putExtra(DetailHistoryActivity.EXTRA_SCAN_HISTORY_ID, scanHistory.id)
            }
            startActivity(intent)
        }

        binding.recyclerViewHistoryHorizontal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyHorizontalAdapter
        }

        // Ambil hanya 5 data terbaru
        historyViewModel.allScanHistory.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isNullOrEmpty()) {
                binding.historyHeaderLayout.visibility = View.GONE
                binding.recyclerViewHistoryHorizontal.visibility = View.GONE
            } else {
                binding.historyHeaderLayout.visibility = View.VISIBLE
                binding.recyclerViewHistoryHorizontal.visibility = View.VISIBLE

                val top5History = historyList.take(5)
                historyHorizontalAdapter = HistoryHorizontalAdapter(top5History) { scanHistory ->
                    val intent = Intent(requireContext(), DetailHistoryActivity::class.java).apply {
                        putExtra(DetailHistoryActivity.EXTRA_SCAN_HISTORY_ID, scanHistory.id)
                    }
                    startActivity(intent)
                }
                binding.recyclerViewHistoryHorizontal.adapter = historyHorizontalAdapter
            }
        }

        // Navigasi ke HistoryFragment ketika klik "Lihat Semua"
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_home, true) // Pastikan untuk menghapus fragment home dari back stack
            .build()

        binding.textSeeAll.setOnClickListener {
            findNavController().navigate(R.id.navigation_history, null, navOptions)
        }

        // Tombol untuk masuk ke ChatbotActivity
        binding.buttonChat.setOnClickListener {
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

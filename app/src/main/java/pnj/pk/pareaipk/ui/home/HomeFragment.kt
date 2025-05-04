package pnj.pk.pareaipk.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pnj.pk.pareaipk.R
import pnj.pk.pareaipk.adapter.HistoryHorizontalAdapter
import pnj.pk.pareaipk.databinding.FragmentHomeBinding
import pnj.pk.pareaipk.ui.chatbot.ChatbotActivity
import pnj.pk.pareaipk.database.entity.HistoryEntity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyHorizontalAdapter: HistoryHorizontalAdapter
    private val historyList = listOf<HistoryEntity>(/* dummy data atau ambil dari ViewModel*/)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root = binding.root

        // Set up RecyclerView for Horizontal Scroll
        historyHorizontalAdapter = HistoryHorizontalAdapter(historyList) { scanHistory ->
            // Handle item click here
            Toast.makeText(requireContext(), "Clicked: ${scanHistory.result}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewHistoryHorizontal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = historyHorizontalAdapter
        }

        // Aksi saat Card 1 diklik
        binding.card1.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Riwayat", Toast.LENGTH_SHORT).show()
        }

        // Aksi saat Card 2 diklik
        binding.card2.setOnClickListener {
            Toast.makeText(requireContext(), "Membuka Kamera", Toast.LENGTH_SHORT).show()
        }

        // Set the button to navigate to ChatbotActivity when clicked
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

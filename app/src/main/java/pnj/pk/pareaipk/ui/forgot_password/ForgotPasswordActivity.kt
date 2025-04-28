package pnj.pk.pareaipk.ui.forgot_password

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pnj.pk.pareaipk.R
import pnj.pk.pareaipk.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observing reset status from ViewModel
        forgotPasswordViewModel.resetStatus.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            if (message.contains("Tautan reset")) {
                finish()  // Close the activity after successful reset email
            }
        }

        // Send Reset Link button click listener
        binding.sendResetLinkButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()

            // Show progress bar when sending reset email
            binding.progressBar.visibility = android.view.View.VISIBLE

            // Send reset password request through ViewModel
            forgotPasswordViewModel.sendPasswordResetEmail(email)

            // Hide progress bar after operation is completed
            binding.progressBar.visibility = android.view.View.GONE
        }

        supportActionBar?.hide()
    }
}
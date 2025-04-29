package pnj.pk.pareaipk.ui.scan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pnj.pk.pareaipk.data.response.ModelMLResponse
import pnj.pk.pareaipk.data.retrofit.ApiConfig
import pnj.pk.pareaipk.utils.reduceFileImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HasilScanViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiConfig.getApiService()

    private val _predictionResult = MutableLiveData<Result<ModelMLResponse>>()
    val predictionResult: LiveData<Result<ModelMLResponse>> = _predictionResult

    fun predictImage(imageFile: File, description: String = "Apple classification") {
        viewModelScope.launch {
            try {
                val compressedFile = imageFile.reduceFileImage()

                val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "image",
                    compressedFile.name,
                    requestImageFile
                )

                val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())

                // Mengambil waktu saat ini
                val currentDateTime = getCurrentDateTime()

                // Memanggil API untuk prediksi
                val response = apiService.predictImage(imageMultipart, descriptionRequestBody)

                if (response.isSuccessful && response.body() != null) {
                    // Jika sukses, update LiveData dengan hasil prediksi
                    _predictionResult.postValue(Result.success(response.body()!!))
                } else {
                    // Jika gagal, post error ke LiveData
                    _predictionResult.postValue(Result.failure(Exception("Failed to get response")))
                }
            } catch (e: Exception) {
                // Tangani error seperti exception saat melakukan request
                _predictionResult.postValue(Result.failure(e))
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}

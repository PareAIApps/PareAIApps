package pnj.pk.pareaipk.data.response

import com.google.gson.annotations.SerializedName

data class ModelMLResponse(
    @SerializedName("class_label")
    val classLabel: String,

    @SerializedName("confidence")
    val confidence: Double,

    @SerializedName("suggestion")
    val suggestion: String,

    @SerializedName("createdAt")
    var createdAt: String
)
package com.example.utttilife.components.Claudinary

import android.app.Application
import com.cloudinary.android.MediaManager

class CloudinaryConfig: Application() {
    override fun onCreate() {
        super.onCreate()
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "dbflxbh3s"
        config["api_key"] = "494141824344153"
        config["api_secret"] = "oXwwJu1B41_3w8QpHURPdKyB6Rw"
        MediaManager.init(this, config)
    }
}
package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.example.recipefinder.R

class DialogLoadingFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the custom dialog layout
        return inflater.inflate(R.layout.dialog_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Lottie animation
        val loadingAnimation = view.findViewById<LottieAnimationView>(R.id.loadingAnimation)
        loadingAnimation.playAnimation()

        // Optional: Prevent the dialog from being canceled
        isCancelable = false
    }
}

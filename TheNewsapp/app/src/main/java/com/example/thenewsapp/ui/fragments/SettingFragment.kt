package com.example.thenewsapp.ui.fragments

import android.content.Context
import com.example.thenewsapp.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.thenewsapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    lateinit var fontSizeSpinner: Spinner
    lateinit var textColorSpinner: Spinner
    lateinit var backgroundColorSpinner: Spinner
    lateinit var saveButton: Button
    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        fontSizeSpinner = view.findViewById(R.id.fontSizeSpinner)
        textColorSpinner = view.findViewById(R.id.textColorSpinner)
        backgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner)
        saveButton = view.findViewById(R.id.saveButton)

        // Populate the Spinners with some default values
        setupSpinners()

        saveButton.setOnClickListener {
            saveSettings()
        }

        return view
    }

    // Method to populate the spinners
    private fun setupSpinners() {
        val fontSizeOptions = arrayOf("Small", "Medium", "Large")
        val textColorOptions = arrayOf("Black", "Blue", "Red")
        val backgroundColorOptions = arrayOf("White", "Black")

        fontSizeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fontSizeOptions)
        textColorSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, textColorOptions)
        backgroundColorSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, backgroundColorOptions)
    }

    private fun saveSettings() {
        val sharedPreferences = requireContext().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Check for null values before calling toString()
        val selectedFontSize = fontSizeSpinner.selectedItem?.toString() ?: "Medium"
        val selectedTextColor = textColorSpinner.selectedItem?.toString() ?: "Black"
        val selectedBackgroundColor = backgroundColorSpinner.selectedItem?.toString() ?: "White"

        // Save selected settings to SharedPreferences
        editor.putString("fontSize", selectedFontSize)
        editor.putString("textColor", selectedTextColor)
        editor.putString("backgroundColor", selectedBackgroundColor)
        editor.apply()

        // Toast message to confirm saving
        Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show()

        // Apply font size to sampleTextView
        binding.sampleTextView.textSize = when (selectedFontSize) {
            "Small" -> 14f
            "Medium" -> 18f
            "Large" -> 22f
            else -> 18f
        }

        // Apply text color
        binding.sampleTextView.setTextColor(
            when (selectedTextColor) {
                "Black" -> Color.BLACK
                "Blue" -> Color.BLUE
                "Red" -> Color.RED
                else -> Color.BLACK
            }
        )

        // Apply background color
        binding.root.setBackgroundColor(
            when (selectedBackgroundColor) {
                "White" -> Color.WHITE
                "Black" -> Color.BLACK
                else -> Color.WHITE
            }
        )
    }
}

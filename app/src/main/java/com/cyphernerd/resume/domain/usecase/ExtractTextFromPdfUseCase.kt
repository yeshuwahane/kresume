package com.cyphernerd.resume.domain.usecase

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import javax.inject.Inject


class ExtractTextFromPdfUseCase @Inject constructor() {
    fun execute(context: Context, pdfUri: Uri): Result<String> {
        return try {

            val inputStream: InputStream? = context.contentResolver.openInputStream(pdfUri)
            inputStream?.use {
                val pdfDocument = PDDocument.load(it)
                val pdfTextStripper = PDFTextStripper().apply {
                    sortByPosition = true
                }
                val text = pdfTextStripper.getText(pdfDocument)
                pdfDocument.close()
                Result.success(text)
            } ?: Result.failure(Exception("Failed to open PDF InputStream"))
        } catch (e: Exception) {
            Log.e("ExtractTextFromPdfUseCase", "Error extracting text from PDF", e)
            Result.failure(e)
        }
    }
}
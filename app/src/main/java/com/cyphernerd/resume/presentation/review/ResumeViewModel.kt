package com.cyphernerd.resume.presentation.review

//import com.cyphernerd.resume.domain.usecase.ExtractTextFromPdfUseCase
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyphernerd.resume.data.util.DataResource
import com.cyphernerd.resume.domain.usecase.AtsScoringUseCase
import com.cyphernerd.resume.domain.usecase.ExtractTextFromPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val extractTextFromPdfUseCase: ExtractTextFromPdfUseCase,
    private val atsScoringUseCase: AtsScoringUseCase
) : ViewModel() {

    private val _resumeTextState = MutableStateFlow(DataResource.initial<String>())
    val resumeTextState = _resumeTextState.asStateFlow()

    private val _atsScoreState = MutableStateFlow(DataResource.initial<Int>())
    val atsScoreState = _atsScoreState.asStateFlow()

    fun extractTextFromPdf(context: Context, pdfUri: Uri,jobDescription: String) {
        viewModelScope.launch {
            _resumeTextState.emit(DataResource.loading())
            val result = extractTextFromPdfUseCase.execute(context, pdfUri)
            result.fold(
                onSuccess = { text ->
                    _resumeTextState.emit(DataResource.success(text))
                    Log.d("viewmodel","text extracted successfully")
                    calculateAtsScore(text,jobDescription)
                },
                onFailure = { error ->
                    _resumeTextState.emit(DataResource.error(error))
                }
            )
        }
    }

    fun calculateAtsScore(resumeText: String, jobDescription: String) {
        _atsScoreState.value = DataResource.loading()
        viewModelScope.launch {
            val result = atsScoringUseCase.execute(resumeText, jobDescription)
            result.fold(
                onSuccess = { score ->
                    _atsScoreState.value = DataResource.success(score)
                },
                onFailure = { exception ->
                    _atsScoreState.value = DataResource.error(exception)
                }
            )
        }
    }


}


data class ResumeState(
    val data: DataResource<String> = DataResource.initial(),
    val score: DataResource<Int> = DataResource.initial()
)



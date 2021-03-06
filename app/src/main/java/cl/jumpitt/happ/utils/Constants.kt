package cl.jumpitt.happ.utils

import cl.jumpitt.happ.R

enum class ColorIdResource(val color: Int) {
    BLACK(R.color.black),
    WHITE(R.color.white),
    BLUE(R.color.blue),
    SKYBLUE(R.color.skyBlue),
    SEMAPHOREORANGE(R.color.semaphoreOrange),
    SEMAPHOREGREEN(R.color.semaphoreGreen),
    SEMAPHORERED(R.color.semaphoreRed)

}

enum class Labelstext(val sizeText: Int, val font: Int, val letterSpacing: Float) {
    H1(R.dimen.h1TextSize, R.font.dmsans_regular, -0.01f),
    H2(R.dimen.h2TextSize, R.font.dmsans_regular, -0.01f),
    H3(R.dimen.h3TextSize, R.font.dmsans_regular, 0.0f),
    H4(R.dimen.h4TextSize, R.font.dmsans_regular, 0.01f),
    H5(R.dimen.h5TextSize, R.font.dmsans_regular, 0.0f),
    H6(R.dimen.h6TextSize, R.font.dmsans_regular, 0.01f),
    BODY1(R.dimen.body1TextSize, R.font.dmsans_regular, 0.0f),
    BODY2(R.dimen.body2TextSize, R.font.dmsans_regular, 0.02f),
    SUBTITLE1(R.dimen.subtitle1TextSize, R.font.dmsans_regular, 0.01f),
    SUBTITLE2(R.dimen.subtitle2TextSize, R.font.dmsans_regular, 0.01f),
    BUTTON(R.dimen.buttonTextSize, R.font.dmsans_medium, 0.04f),
    CAPTION(R.dimen.captionTextSize, R.font.dmsans_regular, 0.03f),
    OVERLINE(R.dimen.overlineTextSize, R.font.dmsans_regular, 0.17f),
}

enum class TriageQuestionType {
    SIMPLE_SELECTION,
    MULTIPLE_SELECTION
}

//APIs
object ConstantsApi{
    const val CLIENT_ID = "90affe9b-de2d-4365-bca3-d6bbda4cf9f4"
    const val CLIENT_SECRET = "S9CQlSPDBbEK9HCS2iYjitAkhlvGwAOPzuq5r24u"
    const val BEARER  = "Bearer"
    const val FAILURE_ERROR = -1
}

enum class SemaphoreTriage(val colorResource: ColorIdResource, val colorID: Int, val level: String){
    RISK_LOW(ColorIdResource.SEMAPHOREGREEN, R.color.semaphoreGreen, "RISK_LOW"),
    RISK_MEDIUM(ColorIdResource.SEMAPHOREORANGE, R.color.semaphoreOrange,"RISK_MEDIUM"),
    RISK_HIGH(ColorIdResource.SEMAPHORERED, R.color.semaphoreRed,"RISK_HIGH")
}

object TriageStatus{
    const val TRIAGE_NOT_STARTED = "TRIAGE_NOT_STARTED"
    const val TRIAGE_PENDING = "TRIAGE_PENDING"
    const val TRIAGE_COMPLETED = "TRIAGE_COMPLETED"
}

object Transition{
    const val CENTER = "center"
    const val LEFT_TO_RIGHT = "left"
    const val RIGHT_TO_LEFT = "right"
}

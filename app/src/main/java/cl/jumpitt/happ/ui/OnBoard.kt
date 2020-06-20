package cl.jumpitt.happ.ui

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import cl.jumpitt.happ.R
import cl.jumpitt.happ.model.OnBoardSlide
import cl.jumpitt.happ.ui.login.Login
import cl.jumpitt.happ.utils.ColorIdResource
import cl.jumpitt.happ.utils.Labelstext
import cl.jumpitt.happ.utils.containedStyle
import cl.jumpitt.happ.utils.goToActivity
import kotlinx.android.synthetic.main.on_board.*

class OnBoard: AppCompatActivity(){

    private lateinit var introOnBoardAdapter: OnBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.on_board)

        introOnBoardAdapter = OnBoardAdapter(
            listOf(
                OnBoardSlide(
                    resources.getString(R.string.lbOnBoardTitle1),
                    resources.getString(R.string.lbOnBoardDescription1),
                    R.drawable.ic_family_group
                )
//                OnBoardSlide(
//                    resources.getString(R.string.lbOnBoardTitle1),
//                    resources.getString(R.string.lbOnBoardDescription1),
//                    R.drawable.ic_buckler_heart
//                ),
//                OnBoardSlide(
//                    resources.getString(R.string.lbOnBoardTitle1),
//                    resources.getString(R.string.lbOnBoardDescription1),
//                    R.drawable.ic_family_group
//                )
            )
        )

        tvTermsConditions.containedStyle(Labelstext.BODY2, ColorIdResource.BLACK, font = R.font.dmsans_medium)
        btnStartOB.containedStyle(ColorIdResource.BLUE, ColorIdResource.WHITE)

        vpIntroOnBoard.adapter = introOnBoardAdapter
//        setupIndicators()
//        setCurrentIndicator(0)
        vpIntroOnBoard.registerOnPageChangeCallback(object:
            ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position )
            }
        })

        btnStartOB.setOnClickListener {
            this.goToActivity<Login>()
        }
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introOnBoardAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(24,0,24,0)
        for(i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))
                this?.layoutParams = layoutParams
            }
            indicatorContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorContainer[i] as ImageView
            if(i == index){
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_active))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.indicator_inactive))

            }
        }
    }







    
}
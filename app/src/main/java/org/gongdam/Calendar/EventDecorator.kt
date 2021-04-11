package org.gongdam.Calendar

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDecorator(st:CalendarDay, en:CalendarDay):DayViewDecorator {
    //val drawable = context.resources.getDrawable()
    //val color = color
    //val dates = dates

    val stDay = st
    val enDay = en

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day!!.isInRange(stDay, enDay)
        /*if(stDay.year != enDay.year){
            ((day!!.month > stDay.month || day.month < enDay.month)
                    || (day.month == stDay.month && day.day >= stDay.day)
                    || (day.month == enDay.month && day.day <= enDay.day))
        }else {
            (
                    (
                            //안으로 겹침. 특가가 더 좁음
                        (day?.year == stDay.year && day.month == stDay.month && day.day >= stDay.day) && (day.year == enDay.year && day.month == enDay.month && day.day <= enDay.day)
                    )
                    || (day?.year == stDay.year && day.year == enDay.year && day.month < enDay.month && day.month > stDay.month))
        }*/
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(object:DotSpan(5f, Color.RED){})
    }
}
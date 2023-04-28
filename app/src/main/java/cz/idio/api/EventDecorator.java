package cz.idio.api;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new CircleSpan(color));
    }
    public class CircleSpan implements LineBackgroundSpan {
        private final int color;

        public CircleSpan(int color) {
            this.color = color;
        }

        @Override
        public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom,
                                   CharSequence text, int start, int end, int lnum) {
            int oldColor = p.getColor();
            p.setColor(color);

            float centerX = ((left + right) / 2);
            float centerY = ((top + bottom) / 2);
            float radius = Math.min((right - left) / 2, (bottom - top) / 2);
            c.drawCircle(centerX, centerY, radius, p);
            p.setColor(oldColor);
        }
    }
}

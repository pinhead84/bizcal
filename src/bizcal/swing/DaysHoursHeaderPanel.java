package bizcal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import bizcal.common.CalendarModel;
import bizcal.common.CalendarViewConfig;
import bizcal.swing.util.ErrorHandler;
import bizcal.swing.util.GradientArea;
import bizcal.swing.util.TrueGridLayout;
import bizcal.util.BizcalException;
import bizcal.util.DateUtil;
import bizcal.util.LocaleBroker;
import bizcal.util.TextUtil;
import bizcal.util.TimeOfDay;

public class DaysHoursHeaderPanel 
{
	public static final Color GRADIENT_COLOR = new Color(230, 230, 230);	
	private PopupMenuCallback popupMenuCallback;
	private JPanel panel;
	private List dateHeaders = new ArrayList();
	private List dateHeaders2 = new ArrayList();
	private List hourHeaders = new ArrayList();
	private List dateList = new ArrayList();
	private List dateLines = new ArrayList();
	private GradientArea gradientArea;
	private JLabel refLabel = new JLabel("AAA");
	private int rowCount;
	private int dayCount;
	private CalendarModel model;
	private Color lineColor = Color.LIGHT_GRAY;
	private int fixedDayCount = -1;
	private CalendarListener listener;
	private boolean showExtraDateHeaders = false;
	private CalendarViewConfig config;
	
	public DaysHoursHeaderPanel(CalendarViewConfig config, CalendarModel model)
	{
		this.config = config;
		this.model = model;
		panel = new JPanel();
		panel.setLayout(new Layout());
		gradientArea = new GradientArea(
				GradientArea.TOP_BOTTOM, Color.WHITE, GRADIENT_COLOR);
		gradientArea.setBorder(false);
	}
		
	public void refresh()
		throws Exception
	{
		dateHeaders.clear();
		dateHeaders2.clear();
		hourHeaders.clear();
		dateList.clear();
		dateLines.clear();
		panel.removeAll();
		
		Calendar calendar = DateUtil.newCalendar();
		dayCount = DateUtil.getDateDiff(model.getInterval().getEndDate(),
				model.getInterval().getStartDate());
		if (fixedDayCount > 0)
			dayCount = fixedDayCount;
		
		if (dayCount > 1) {
			rowCount = 1;
			DateFormat toolTipFormat = new SimpleDateFormat("EEEE d MMMM",
					LocaleBroker.getLocale());
			DateFormat dateFormat = 
				DateFormat.getDateInstance(DateFormat.SHORT, LocaleBroker.getLocale());
			DateFormat hourFormat = new SimpleDateFormat("HH"); 
			if (dayCount == 5 || dayCount == 7) {
			}
			JPanel dateHeaderPanel = new JPanel();
			dateHeaderPanel.setLayout(new TrueGridLayout(1, dayCount));
			dateHeaderPanel.setOpaque(false);
			Date date = model.getInterval().getStartDate();
			if (fixedDayCount > 0)
				date = DateUtil.round2Week(date);
			for (int i = 0; i < dayCount; i++) {
				String dateStr = dateFormat.format(date);
				JLabel header = new JLabel(dateStr, JLabel.CENTER);
				header.setToolTipText(toolTipFormat.format(date));
				if (model.isRedDay(date))
					header.setForeground(Color.RED);
				dateHeaders.add(header);
				panel.add(header);
				/*if (showExtraDateHeaders) {
					header = new JLabel(model.getDateHeader(cal.getId(), date), JLabel.CENTER);
					dateHeaders2.add(header);
					panel.add(header);
				}*/
				dateList.add(date);
				long time = config.getStartView().getValue();
				while (time < config.getEndView().getValue()) {					
					dateStr = hourFormat.format(new TimeOfDay(time).getDate(date));
					header = new JLabel(dateStr, JLabel.CENTER);
					dateHeaders2.add(header);
					panel.add(header);
					if (i > 0 || time > config.getStartView().getValue()) {
						JLabel line = new JLabel();
						line.setBackground(lineColor);
						line.setOpaque(true);
						line.setBackground(lineColor);
						if (DateUtil.getDayOfWeek(date) == calendar.getFirstDayOfWeek()) 
							line.setBackground(DayView.LINE_COLOR_DARKER);
						if (model.getSelectedCalendars().size() > 1 && i == 0)
							line.setBackground(DayView.LINE_COLOR_EVEN_DARKER);						
						panel.add(line);
						dateLines.add(line);
					}					
					time += 3600*1000;
				}
				date = DateUtil.getDiffDay(date, +1);
			}
		}
		
		if (showExtraDateHeaders)
			rowCount++;

		panel.add(gradientArea);
		panel.updateUI();
	}
	
	public JComponent getComponent()
	{
		return panel;
	}

	protected class CalHeaderMouseListener extends MouseAdapter {
		private Object calId;

		public CalHeaderMouseListener(Object calId) {
			this.calId = calId;
		}

		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			try {
				if (e.isPopupTrigger()) {
					JPopupMenu popup = popupMenuCallback
							.getCalendarPopupMenu(calId);
					if (popup == null)
						return;
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			} catch (Exception exc) {
				throw BizcalException.create(exc);
			}
		}

		public void mouseEntered(MouseEvent e) {
			//rootPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		public void mouseExited(MouseEvent e) {
			//rootPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private class Layout implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension preferredLayoutSize(Container parent) {
			try {
				int height = refLabel.getPreferredSize().height;
				height = rowCount * height;
				int width = dayCount * model.getSelectedCalendars().size() * DayView.PREFERRED_DAY_WIDTH;
				System.err.println("DayHoursHeaderPanel: height=" + height);
				return new Dimension(width, height);
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 100);
		}

		public void layoutContainer(Container parent) {
			try {
				if (rowCount == 0)
					return;
				double totWidth = parent.getWidth();
				double dateColWidth = totWidth / dateHeaders.size();
				double rowHeight = parent.getHeight() / rowCount;
				int dateI = 0;
				int dateLineI = 0;
				int dayRowCount = showExtraDateHeaders ? 2 : 1;
				for (int j=0; j < dayCount; j++) {
					JLabel dateLabel = (JLabel) dateHeaders.get(dateI);
					int xpos = (int) (dateI*dateColWidth);
					dateLabel.setBounds(xpos,
							(int) 0,
							(int) dateColWidth, 
							(int) rowHeight);
					/*if (showExtraDateHeaders) {
						dateLabel = (JLabel) dateHeaders2.get(dateI);
						dateLabel.setBounds(xpos,
								(int) (dateYPos + rowHeight),
								(int) dateColWidth, 
								(int) rowHeight);
					}*/
					long time = config.getStartView().getValue();
					while (time < config.getEndView().getValue()) {					
						if (j > 0 || time > config.getStartView().getValue()) {
							dateLabel = (JLabel) dateHeaders.get(dateI);
							xpos = (int) (dateI*dateColWidth);
							dateLabel.setBounds(xpos,
									(int) rowHeight,
									(int) dateColWidth, 
									(int) rowHeight);							
							JLabel line = (JLabel) dateLines.get(dateLineI);
							int height = (int) rowHeight * dayRowCount;
							int ypos = (int) rowHeight;
							if (j == 0) {
								ypos = 0;
								height = (int) (rowHeight*(dayRowCount+1));
							}
							line.setBounds(xpos, 
									ypos,
									1,
									height);
							dateLineI++;
						}
						time += 3600*1000;					
					}					
					dateI++;
				}
				gradientArea.setBounds(0, 0, parent.getWidth(), parent.getHeight());
				resizeDates((int) dateColWidth);
			} catch (Exception e) {
				throw BizcalException.create(e);
			}
		}
	}
	
	private void resizeDates(int width)
		throws Exception
	{
		if (dayCount != 5 && dayCount != 7)
			return;

		Date today = DateUtil.round2Day(new Date());
		
		FontMetrics metrics = refLabel.getFontMetrics(refLabel.getFont());
		int charCount = 10;
		if (maxWidth(charCount, metrics) > width) {
			charCount = 3;
			if (maxWidth(charCount, metrics) > width) {
				charCount = 2;
				if (maxWidth(charCount, metrics) > width) {
					charCount = 1;
				}
			}
		}
		DateFormat format = new SimpleDateFormat("EEEEE");
		for (int i=0; i < dateHeaders.size(); i++) {
			JLabel label = (JLabel) dateHeaders.get(i);
			Date date = (Date) dateList.get(i);
			String str = format.format(date);
			if (str.length() > charCount)
				str = str.substring(0, charCount);
			str = TextUtil.formatCase(str);
			if (today.equals(DateUtil.round2Day(date)))
				str = "<html><b>" + str + "</b></html>";
			label.setText(str);
		}
	}
	
	private int maxWidth(int charCount, FontMetrics metrics)
		throws Exception
	{
		DateFormat format = new SimpleDateFormat("EEEEE", LocaleBroker.getLocale());
		Calendar cal = DateUtil.newCalendar();
		cal.set(Calendar.DAY_OF_WEEK, 1);
		int maxWidth = 0;
		for (int i=0; i < 7; i++) {
			String str = format.format(cal.getTime());
			if (str.length() > charCount)
				str = str.substring(0, charCount);
			int width = metrics.stringWidth(str);
			if (width > maxWidth)
				maxWidth = width;
			cal.add(Calendar.DAY_OF_WEEK, +1);
		}
		return maxWidth;
	}
	
	public void setModel(CalendarModel model) {
		this.model = model;
	}
	public void setPopupMenuCallback(PopupMenuCallback popupMenuCallback) {
		this.popupMenuCallback = popupMenuCallback;
	}
	
	public void addCalendarListener(CalendarListener listener)
	{
		this.listener = listener;
	}
	
	private class CloseListener
		extends MouseAdapter
	{
		private Object calId;
		
		public CloseListener(Object calId)
		{
			this.calId = calId;
		}
		
		public void mouseClicked(MouseEvent event)
		{
			try {
				listener.closeCalendar(calId);
			} catch (Exception e) {
				ErrorHandler.handleError(e);
			}
		}
	}

	public void setShowExtraDateHeaders(boolean showExtraDateHeaders) {
		this.showExtraDateHeaders = showExtraDateHeaders;
	}
	
}
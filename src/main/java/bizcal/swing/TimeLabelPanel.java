/*******************************************************************************
 * Bizcal is a component library for calendar widgets written in java using swing.
 * Copyright (C) 2007  Frederik Bertilsson 
 * Contributors:       Martin Heinemann martin.heinemann(at)tudor.lu
 * 
 * http://sourceforge.net/projects/bizcal/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 * 
 *******************************************************************************/
package bizcal.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bizcal.common.CalendarViewConfig;
import bizcal.common.DayViewConfig;
import bizcal.swing.util.GradientArea;
import bizcal.util.TimeOfDay;

/**
 * 
 * Class to paint the time labels on the border of the calendar grid.
 * 
 * @author martin.heinemann@tudor.lu
 * 27.03.2008
 * 10:45:06
 *
 *
 * @version
 * <br>$Log: TimeLabelPanel.java,v $
 * <br>Revision 1.9  2009/05/11 16:11:18  heine_
 * <br>nicer time row labeling for different hour fragmentations.
 * <br>
 * <br>Revision 1.8  2009/02/02 12:38:21  heine_
 * <br>changed time ruler. The hour lables are now placed in the right position of the hour.
 * <br>
 * <br>Revision 1.7  2008/06/12 13:04:18  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.6  2008/03/28 08:45:11  heine_
 * <br>*** empty log message ***
 * <br>
 *   
 */
public class TimeLabelPanel
{
	private JPanel panel;
	private List<JLabel> hourLabels = new ArrayList<JLabel>();
	private List<JLabel> minuteLabels = new ArrayList<JLabel>();
	private List<JLabel> hourLines = new ArrayList<JLabel>();
	private List<JLabel> minuteLines = new ArrayList<JLabel>();
	private Font font = new Font("Verdana", Font.PLAIN, 11);
	private GradientArea gradientArea;
	private int width = 40;
	private int hourCount;
	private int footerHeight = 0;
	private CalendarViewConfig config;
	private TimeOfDay start;
	private TimeOfDay end;
	private SimpleDateFormat hourFormat;
	private Font hourFont;
	private int timeslots;

	public TimeLabelPanel(CalendarViewConfig config, TimeOfDay start, TimeOfDay end, int timeslots) throws Exception {
		/* ================================================== */
		this.config = config;
		this.start = start;
		this.end = end;
		this.timeslots = timeslots;
		/* ------------------------------------------------------- */
		panel = new JPanel();
		panel.setLayout(new Layout());

		this.hourFormat = new SimpleDateFormat("HH");
		hourFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		this.hourFont = font.deriveFont((float) 12);
		hourFont = hourFont.deriveFont(Font.BOLD);
		/* ------------------------------------------------------- */
		refresh();
		/* ================================================== */
	}


	public void refresh() {
		/* ================================================== */
		try {
			hourCount = Math.abs(end.getHour() - start.getHour());

			if (hourCount == 0)
				hourCount = 24;
			/* ------------------------------------------------------- */
			// clear arrays
			hourLabels.clear();
			minuteLabels.clear();
			hourLines.clear();
			minuteLines.clear();
			/* ------------------------------------------------------- */
			// remove all elemtns from panel
			panel.removeAll();
			/* ------------------------------------------------------- */
			long pos = start.getValue();
	 		while (pos < end.getValue()) {
	 			/* ------------------------------------------------------- */
	 			// new try, hour label and 30.
	 			/* ------------------------------------------------------- */
	 			Date date = new Date(pos);
				String timeTxt = hourFormat.format(date);
				/* ------------------------------------------------------- */
				// make the hour label nicer
				/* ------------------------------------------------------- */
				JLabel timeLabel = new JLabel(timeTxt);
				timeLabel.setVerticalTextPosition(JLabel.CENTER);
				timeLabel.setFont(hourFont);
				/* ------------------------------------------------------- */
				panel.add(timeLabel);
				hourLabels.add(timeLabel);
				/* ------------------------------------------------------- */
				// a label for the line of the hour
				/* ------------------------------------------------------- */
				JLabel line = new JLabel();
				line.setBackground(this.config.getLineColor());
				line.setOpaque(true);
				/* ------------------------------------------------------- */
				panel.add(line);
				hourLines.add(line);
				/* ------------------------------------------------------- */
				// manages the display for different hour fragmentations
				/* ------------------------------------------------------- */
				switch (this.timeslots) {
					case DayViewConfig.FRAG_HOUR: {
									/* ------------------------------------------------------- */
									// for hours, do nothing. we just need the hour labes
									// that are already added to the panel above
									/* ------------------------------------------------------- */
									break;
									/* ------------------------------------------------------- */

					}
					case DayViewConfig.FRAG_HALF: {
						/* ------------------------------------------------------- */
						// half hour fragmenation. we need one minute line and the "30"
						// label
						/* ------------------------------------------------------- */
						createTimeLabel("30");
						createMinuteLine();
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_THIRD: {
						/* ------------------------------------------------------- */
						// 20 min fragmentation. We need 2 lines and "20" + "40" labels
						/* ------------------------------------------------------- */
						createTimeLabel("20");
						createTimeLabel("40");
						createMinuteLine();
						createMinuteLine();
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_QUARTER: {
						/* ------------------------------------------------------- */
						// 15 min fragmentation. We need 3 lines and the "30" label
						/* ------------------------------------------------------- */
						createTimeLabel("30");
						createMinuteLine();
						createMinuteLine();
						createMinuteLine();
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_SIXTHT: {
						/* ------------------------------------------------------- */
						// 10 min fragmentation. We need 5 lines and the "20" + "40" labels
						/* ------------------------------------------------------- */
						createTimeLabel("20");
						createTimeLabel("40");
						createMinuteLine();
						createMinuteLine();
						createMinuteLine();
						createMinuteLine();
						createMinuteLine();
						break;
						/* ------------------------------------------------------- */
					}
				default:
					break;
				}
				
				
//				createTimeLabel("30");
//				createMinuteLine();
//				createMinuteLine();
//				createMinuteLine();
//				createMinuteLine();
				
				pos += 3600 * 1000;
	 			
	 			/* ------------------------------------------------------- */
//				Date date = new Date(pos);
//				String timeTxt = hourFormat.format(date);
//				JLabel timeLabel = new JLabel(timeTxt);
//				timeLabel.setVerticalTextPosition(JLabel.CENTER);
//				timeLabel.setFont(hourFont);
//				panel.add(timeLabel);
//				hourLabels.add(timeLabel);
//				JLabel line = new JLabel();
//				line.setBackground(this.config.getLineColor());
//				line.setOpaque(true);
//				hourLines.add(line);
//				panel.add(line);
//				
//
//				timeTxt = "15";
//				timeLabel = new JLabel(timeTxt);
//				timeLabel.setFont(font);
//				panel.add(timeLabel);
//				minuteLabels.add(timeLabel);
//				createMinuteLine();
//				createMinuteLine();
//
//				timeTxt = "45";
//				timeLabel = new JLabel(timeTxt);
//				timeLabel.setFont(font);
//				panel.add(timeLabel);
//				minuteLabels.add(timeLabel);
//				createMinuteLine();
//				createMinuteLine();
//
//				pos += 3600 * 1000;
			}
	        gradientArea = new GradientArea(GradientArea.LEFT_RIGHT, Color.WHITE,
	        		ColumnHeaderPanel.GRADIENT_COLOR);
	        gradientArea.setOpaque(true);
			gradientArea.setBorder(false);
			panel.add(gradientArea);


			panel.validate();
			panel.updateUI();

		} catch (Exception e) {
			e.printStackTrace();
		}
		/* ================================================== */
	}

	
	/**
	 * Creates a label with a time text and adds it to the panel and
	 * the minuteLabels list
	 * 
	 * @param time
	 * @return the created label
	 */
	private JLabel createTimeLabel(String time) {
		/* ====================================================== */
		JLabel timeLabel = new JLabel(time);
		timeLabel.setFont(font);
		panel.add(timeLabel);
		minuteLabels.add(timeLabel);
		return timeLabel;
		/* ====================================================== */
	}


	/**
	 * Creates a new JLabel for a line and adds it to the panel
	 */
	private void createMinuteLine() {
		/* ================================================== */
		JLabel line = new JLabel();
		line.setBackground(this.config.getLineColor());
		line.setOpaque(true);
		minuteLines.add(line);
		panel.add(line);
		/* ================================================== */
	}
	
	
	/**
	 * Sets the start end end interval.
	 * A refresh is made automatically
	 *
	 * @param start
	 * @param end
	 */
	public void setStartEnd(TimeOfDay start, TimeOfDay end, int timeslots) {
		/* ================================================== */
		this.start = start;
		this.end = end;
		this.timeslots = timeslots;
		refresh();
		/* ================================================== */
	}

	private int getPreferredHeight()
	{
		return DayView.PIXELS_PER_HOUR * hourCount + footerHeight;
	}

	private class Layout implements LayoutManager {
		
		private int colWidth = width /2;
		private double rowHeight = 0;
		
		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(width, getPreferredHeight());
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(50, 50);
		}

		public void layoutContainer(Container parent) {
			/* ================================================== */
			try {
			double totHeight = parent.getHeight() - footerHeight;
			this.rowHeight = totHeight / hourCount;
			double minuteRowHeight = rowHeight / 2;
			this.colWidth = width / 2;
			Integer iMinute = 0;
			int iLine  = 0;
			for (int i=0; i < hourLabels.size(); i++) {
				/* ------------------------------------------------------- */
				// layout the hour labels
				/* ------------------------------------------------------- */
				JLabel hourLabel = (JLabel) hourLabels.get(i);
				hourLabel.setBounds(0,
//						(int) (i*rowHeight),
						(int) ((i)*rowHeight) -7,
						colWidth,
						(int) 15);
				/* ------------------------------------------------------- */
				// layout the hour lines
				/* ------------------------------------------------------- */
				JLabel hourLine = (JLabel) hourLines.get(i);
				hourLine.setBounds(colWidth,
						(int) ((i+1)*rowHeight),
						width,
						1);
				/* ------------------------------------------------------- */
				// layout according the timeslots
				/* ------------------------------------------------------- */
				switch (timeslots) {
					case DayViewConfig.FRAG_HOUR:  {
						/* ------------------------------------------------------- */
						// hour, no line, no label
						/* ------------------------------------------------------- */
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_HALF:  {
						/* ------------------------------------------------------- */
						// one line, 30 label
						/* ------------------------------------------------------- */
						// layout the 30 minute label
						/* ------------------------------------------------------- */
						iMinute = layoutMinuteLabel(i, iMinute);
						/* ------------------------------------------------------- */
						// the minute line for the 30 min
						/* ------------------------------------------------------- */
						iLine = layoutMinuteLine(i, iLine);
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_THIRD:  {
						/* ------------------------------------------------------- */
						// 2 lines, 20 and 40 label
						/* ------------------------------------------------------- */
						// layout the 20 minute label
						/* ------------------------------------------------------- */
						iMinute = layoutMinuteLabel(i, iMinute, (int) ((rowHeight/6)),(int) ((rowHeight/6) * 2));
						// 40 minute label
						iMinute = layoutMinuteLabel(i, iMinute, (int) ((rowHeight/6)*3),(int) ((rowHeight/6) * 2));
						/* ------------------------------------------------------- */
						// line layout
						/* ------------------------------------------------------- */
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/3)),   10);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/3)*2), 10);
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_QUARTER:  {
						/* ------------------------------------------------------- */
						// 3 lines, and "30" label
						/* ------------------------------------------------------- */
						// layout the 30 minute label
						/* ------------------------------------------------------- */
						iMinute = layoutMinuteLabel(i, iMinute);
						/* ------------------------------------------------------- */
						// line layout
						/* ------------------------------------------------------- */
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/4)),   10);
						iLine = layoutMinuteLine(i, iLine);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/4)*3),   10);
						break;
						/* ------------------------------------------------------- */
					}
					case DayViewConfig.FRAG_SIXTHT:  {
						/* ------------------------------------------------------- */
						// 5 lines, and "20" + "40" label
						/* ------------------------------------------------------- */
						// layout the 20 minute label
						/* ------------------------------------------------------- */
						iMinute = layoutMinuteLabel(i, iMinute, (int) ((rowHeight/6)),(int) ((rowHeight/6) * 2));
						// 40 minute label
						iMinute = layoutMinuteLabel(i, iMinute, (int) ((rowHeight/6)*3),(int) ((rowHeight/6) * 2));
						/* ------------------------------------------------------- */
						// line layout
						/* ------------------------------------------------------- */
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/6)),       10);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/6) * 2),    5);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/6) * 3),   10);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/6) * 4),    5);
						iLine = layoutMinuteLine(i, iLine, (int) ((rowHeight/6) * 5),   10);
						break;
						/* ------------------------------------------------------- */
					}
				default:
					break;
				}
				/* ------------------------------------------------------- */
			}
			gradientArea.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
			/* ================================================== */
		}
		
		
		/**
		 * Layouts a minute label
		 * 
		 * @param hourPosition
		 * @param minutePosition
		 */
		private int layoutMinuteLabel(int hourPosition, int minutePosition) {
			/* ================================================== */
			return layoutMinuteLabel(hourPosition, minutePosition, 0, (int) rowHeight);
			/* ================================================== */
		}
		
		/**
		 * Layouts a minute label. yOffset defines the distance inside an hour for 
		 * the position of the label. Start at the hour label.
		 * 
		 * @param hourPosition
		 * @param minutePosition
		 * @param yInside  startposition inside an hour
		 */
		private int layoutMinuteLabel(int hourPosition, int minutePosition, int yInside, int height) {
			/* ================================================== */
			JLabel minuteLabel = (JLabel) minuteLabels.get(minutePosition);
			minuteLabel.setBounds(colWidth - 15,
					(int) (hourPosition*rowHeight) + yInside,
					colWidth,
					(int) height);
			minutePosition++;
			return minutePosition;
			/* ================================================== */
		}
		
		/**
		 * Layouts a minute line in the middle of an hour
		 * 
		 * @param hourPosition
		 * @param linePostion
		 */
		private int layoutMinuteLine(int hourPosition, int linePostion) {
			/* ================================================== */
			return layoutMinuteLine(hourPosition, linePostion, (int) (rowHeight / 2), 5);
			/* ================================================== */
		}
		
		/**
		 * Layouts a minute line
		 * 
		 * @param hourPosition
		 * @param linePostion
		 * @param yInside position inside an hour
		 * @param length x ofset of the length of the line. The larger the value, the shorter the line
		 * @return
		 */
		private int layoutMinuteLine(int hourPosition, int linePostion, int yInside, int length) {
			/* ================================================== */
			JLabel minuteLine = (JLabel) minuteLines.get(linePostion);
			minuteLine.setBounds(colWidth + length,
					(int) ((hourPosition*rowHeight) + yInside),
					colWidth,
					1);
			linePostion++;
			return linePostion;
			/* ================================================== */
		}
		
	}
	
	
	
	
	public JComponent getComponent()
	{
		return panel;
	}

	public void setFooterHeight(int footerHeight) {
		this.footerHeight = footerHeight;
	}
}

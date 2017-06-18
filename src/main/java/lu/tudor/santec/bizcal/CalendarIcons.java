/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu
 * Thorsten Roth thorsten.roth(at)tudor.lu  
 *  
 * This library is free software; you can redistribute it and/or modify it  
 * under the terms of the GNU Lesser General Public License (version 2.1)
 * as published by the Free Software Foundation.
 * 
 * This software is distributed in the hope that it will be useful, but     
 * WITHOUT ANY WARRANTY; without even the implied warranty of               
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU        
 * Lesser General Public License for more details.                          
 * 
 * You should have received a copy of the GNU Lesser General Public         
 * License along with this library; if not, write to the Free Software      
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *******************************************************************************/
package lu.tudor.santec.bizcal;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * @author martin.heinemann@tudor.lu
 *
 * @version
 * <br>$Log: CalendarIcons.java,v $
 * <br>Revision 1.5  2011/03/04 12:45:35  thorstenroth
 * <br>1. Improvement of the mouse controls when event gets resize and move in the calendar.
 * <br>2. Bug Fix: The position of the current timeline is now correct and only shown ar the current day.
 * <br>3. Bug Fix: Because of the bug the view can not difference between Events form different calendars which have the same start and end time so sometimes by resize or move a event there are side effects when drawing the events.
 * <br>
 * <br>Revision 1.4  2011/02/22 14:59:32  thorstenroth
 * <br>1. Add a new layout for the day view. This layout split the day column into a number of lines which is equal to the number of calendars which are active. The events of one calendar are now shown in one line, one below the other.
 * <br>
 * <br>2. Add a new horizontal line to the day view to represent the current time.
 * <br>
 * <br>Revision 1.3  2011/02/11 07:22:07  thorstenroth
 * <br>Add a new view to the calendar the 'Three Day View' which shows three days per interval.
 * <br>
 */
public class CalendarIcons {

//	***************************************************************************
//	* Class Constants			                                              *
//	***************************************************************************

		protected static final int LARGEPIX = 64;

		protected static final int MEDIPIX = 24;

		protected static final int SMALLPIX = 18;

		protected static final int MINIPIX = 12;

		public static String AGENDA 				= "agendamodule.png";
		public static final String DAYVIEW 			= "cal_day.png";
		public static final String THREEDAYVIEW 	= "cal_three_day.png";
		public static final String WEEKVIEW 		= "cal_week.png";
		public static final String MONTHVIEW 		= "cal_month.png";
		public static final String LISTVIEW 		= "cal_list.png";

		public static final String TODAY 			= "today.png";


		// day views
		public static final String DAY_FULL 		= MONTHVIEW;
		public static final String DAY_MORNING 		= "cal_day_morning.png";
		public static final String DAY_AFTERNOON 	= "cal_day_afternoon.png";


		public static String NEW 					= "add_line.png";
		public static String EDIT 					= "edit.png";
		public static String DELETE 				= "close.png";

		public static String COPY 					= "copy.png";
		public static String PASTE					= "paste.png";
		
		public static String CHANGE_LAYOUT_MODE		= "newrecurevent.png"; //TODO Test the layoutbuttons
		
		public static String CURSOR_RESIZE			= "cursor_resize_new.png";

//	---------------------------------------------------------------------------

	public static ImageIcon getIcon (String p_IconName)
		{
		String 		l_IconPath;
		URL 			l_Location;
		ImageIcon 	l_Icon = null;

		l_IconPath = "resources/icons/" + p_IconName;
		l_Location = CalendarIcons.class.getResource(l_IconPath);

		if (l_Location != null) l_Icon = new ImageIcon(l_Location);

		if ((l_Icon == null) || (l_Icon.getIconHeight() <= 0))
			{
			System.out.println("Couldn't find Icon: " + l_IconPath);
			}
		return l_Icon;
		}

//	---------------------------------------------------------------------------

	public static ImageIcon getScaledIcon(String p_IconName, int size)
		{
		return new ImageIcon(getIcon(p_IconName).getImage().getScaledInstance(
				size, size, Image.SCALE_SMOOTH));
		}

//	---------------------------------------------------------------------------

		public static ImageIcon getMiniIcon(String p_IconName) {
			return getScaledIcon(p_IconName, MINIPIX);
		}

//	---------------------------------------------------------------------------

		public static ImageIcon getSmallIcon(String p_IconName) {
			return getScaledIcon(p_IconName, SMALLPIX);
		}

//	---------------------------------------------------------------------------

		public static ImageIcon getMediumIcon(String p_IconName) {
			return getScaledIcon(p_IconName, MEDIPIX);
		}

//	---------------------------------------------------------------------------

		public static ImageIcon getBigIcon(String p_IconName) {
			return getScaledIcon(p_IconName, LARGEPIX);
		}


}

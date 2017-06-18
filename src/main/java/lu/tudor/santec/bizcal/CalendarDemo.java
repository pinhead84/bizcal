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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import lu.tudor.santec.bizcal.listeners.NamedCalendarListener;
import lu.tudor.santec.bizcal.util.ObservableEventList;
import lu.tudor.santec.bizcal.views.DayViewPanel;
import lu.tudor.santec.bizcal.views.ListViewPanel;
import lu.tudor.santec.bizcal.views.MonthViewPanel;
import bizcal.common.Event;
import bizcal.swing.CalendarListener;
import bizcal.swing.DayView;
import bizcal.swing.DayView.Layout;
import bizcal.swing.util.FrameArea;
import bizcal.util.DateInterval;

/**
 * @author martin.heinemann@tudor.lu
 * 08.04.2008
 * 11:38:08
 *
 *
 * @version
 * <br>$Log: CalendarDemo.java,v $
 * <br>Revision 1.14  2012/04/24 13:49:49  thorstenroth
 * <br>1. Add border to class NamedCalendar to bordered a select Calendar in the calendar panel.
 * <br>2. Fix Bug: Sometimes if a agenda entry has been selected the calendar of the entry was not selected. Now: The Calendar will be selected if a entry of the calendar is selected.
 * <br>
 * <br>Revision 1.13  2011/10/20 15:32:21  thorstenroth
 * <br>1. add new calendar type the background calendar type which is displayed over a whole column.
 * <br>2. fix Bug: public holidays are not displayed over the whole daily column
 * <br>
 * <br>Revision 1.12  2011/03/04 12:45:35  thorstenroth
 * <br>1. Improvement of the mouse controls when event gets resize and move in the calendar.
 * <br>2. Bug Fix: The position of the current timeline is now correct and only shown ar the current day.
 * <br>3. Bug Fix: Because of the bug the view can not difference between Events form different calendars which have the same start and end time so sometimes by resize or move a event there are side effects when drawing the events.
 * <br>
 * <br>Revision 1.11  2011/02/22 14:59:32  thorstenroth
 * <br>1. Add a new layout for the day view. This layout split the day column into a number of lines which is equal to the number of calendars which are active. The events of one calendar are now shown in one line, one below the other.
 * <br>
 * <br>2. Add a new horizontal line to the day view to represent the current time.
 * <br>
 * <br>Revision 1.10  2011/02/11 07:22:07  thorstenroth
 * <br>Add a new view to the calendar the 'Three Day View' which shows three days per interval.
 * <br>
 * <br>Revision 1.9  2009/04/28 14:11:26  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.8  2008/06/19 12:20:00  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.7  2008/06/09 14:10:09  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.6  2008/05/30 11:36:47  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.5  2008/04/08 13:17:53  heine_
 * <br>*** empty log message ***
 * <br>
 *   
 */
public class CalendarDemo extends JFrame{

	private static final long serialVersionUID = 1L;
	
	
		
	private ObservableEventList eventDataList;

	private DayViewPanel dayViewPanel;

	private DayViewPanel weekViewPanel;

	private MonthViewPanel monthViewPanel;

	private ListViewPanel listViewPanel;

	private CalendarPanel calendarPanel;

	private DayViewPanel dayThreeViewPanel;

	public CalendarDemo() {
		/* ================================================== */
		super("Calendar Demo");
		
		this.calendarPanel = new CalendarPanel();
		
		/* ------------------------------------------------------- */
		// this is the "data base" for all events. All created events 
		// will be stored in this list
		/* ------------------------------------------------------- */
		this.eventDataList = new ObservableEventList();
		/* ------------------------------------------------------- */
		// create a model for each view day, week, month, list
		// they all gain the same data list to operate on
		/* ------------------------------------------------------- */
		EventModel dayModel   		= new EventModel(eventDataList, EventModel.TYPE_DAY);
		EventModel weekModel  		= new EventModel(eventDataList, EventModel.TYPE_WEEK);
		EventModel monthModel 		= new EventModel(eventDataList, EventModel.TYPE_MONTH);
		EventModel listModel  		= new EventModel(eventDataList, EventModel.TYPE_MONTH);
		EventModel dayThreeModel 	= new EventModel(eventDataList, EventModel.TYPE_THREE_DAY);
		
		/* ------------------------------------------------------- */
		// create the panels for each kind of view
		/* ------------------------------------------------------- */
		this.dayViewPanel 		= new DayViewPanel(dayModel, 		Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.dayThreeViewPanel 	= new DayViewPanel(dayThreeModel, 	Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.weekViewPanel 		= new DayViewPanel(weekModel, 		Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
		this.monthViewPanel 	= new MonthViewPanel(monthModel);
		this.listViewPanel 		= new ListViewPanel(listModel);
		/* ------------------------------------------------------- */
		// create a new calendar listener.
		// It will be informed of many interactions on the calendar like, event selected
		// copy & paste, date changed etc. Have a look at the interface
		/* ------------------------------------------------------- */
		DemoCalendarListener calListener = new DemoCalendarListener();
		
		/* ------------------------------------------------------- */
		// add the same listener to all views
		// you can create different listeners for each view, if you like to.
		/* ------------------------------------------------------- */
		this.dayViewPanel.addCalendarListener(		calListener);
		this.dayThreeViewPanel.addCalendarListener(	calListener);
		this.weekViewPanel.addCalendarListener(		calListener);
		this.monthViewPanel.addCalendarListener(	calListener);
		this.listViewPanel.addCalendarListener(		calListener);

		/* ------------------------------------------------------- */
		// now we add all views to the base panel
		/* ------------------------------------------------------- */
		this.calendarPanel.addCalendarView(dayViewPanel);
		this.calendarPanel.addCalendarView(dayThreeViewPanel);
		this.calendarPanel.addCalendarView(weekViewPanel);
		this.calendarPanel.addCalendarView(monthViewPanel);
		this.calendarPanel.addCalendarView(listViewPanel);
		/* ------------------------------------------------------- */
		
		/* ------------------------------------------------------- */
		// now we create some sample calendars.
		// they will appear in the right bar.
		/* ------------------------------------------------------- */
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar("Peter", 	"dem Peter seiner", Color.RED));
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar("Max", 	"dem Max seiner", 	Color.BLUE));
		this.calendarPanel.addNamedCalendar(new TestNamedCalendar("Office", "allen ihrer", 		Color.GRAY));
		/* ------------------------------------------------------- */
		// next step is to create a listener that is responsible for selecting and deselecting of
		// the calendars created above.
		//
		// we distinguish between active and selected calendars.
		// An active calendar is allowed to display its events on the views
		// A selected calendar is the calendar which will recieve the actions on the view, 
		// like creating a new event, moving, deleting etc.
		/* ------------------------------------------------------- */
		this.calendarPanel.addNamedCalendarListener(new NamedCalendarListener() {

			public void activeCalendarsChanged(Collection<NamedCalendar> calendars) {
				/* ====================================================== */
				// if no calendar is active, remove all events
				/* ------------------------------------------------------- */
				if (calendars == null || calendars.size() < 1) {
					eventDataList.clear();
					return;
				}
				/* ------------------------------------------------------- */
				// fetch the appointments of the active calendars
				/* ------------------------------------------------------- */
				updateEventsForActiveCalendars();
				/* ====================================================== */
			}

			public void selectedCalendarChanged(NamedCalendar selectedCalendar) {
				/* ====================================================== */
				// we do nothing here.
				// If you have any ideas of something that should be triggerd when a calendar was selected...
				/* ====================================================== */
				
				updateEventsForActiveCalendars();
			}

		});

		///////////////////////////////////////////////////////////////////////////
		// TODO add some Buttons to test different view layouts of the day views //
		Action layout0Action;
		Action layout1Action;
		Action layout2Action;
		
		layout0Action = new AbstractAction("0", CalendarIcons
				.getMediumIcon(CalendarIcons.CHANGE_LAYOUT_MODE)) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("### layout mode 0 ###");
				DayView currentDayView = (DayView) calendarPanel.getCurrentView().getView();
				currentDayView.setLayoutMode(Layout.DAY_COLUMN_NORMAL);
				try {
					currentDayView.refresh0();
				} catch (Exception e1) {
					e1.printStackTrace();
				}	
			}
		};

		calendarPanel.getFunctionsButtonPanel().addAction(layout0Action);
		
		layout1Action = new AbstractAction("1", CalendarIcons
				.getMediumIcon(CalendarIcons.CHANGE_LAYOUT_MODE)) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				System.out.println("### layout mode 1 ###");
				DayView currentDayView = (DayView) calendarPanel.getCurrentView().getView();
				currentDayView.setLayoutMode(Layout.DAY_COLUMN_SEPARATED_BY_CALENDAR);
				try {
					currentDayView.refresh0();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};

		calendarPanel.getFunctionsButtonPanel().addAction(layout1Action);
		
		layout2Action = new AbstractAction("2", CalendarIcons
				.getMediumIcon(CalendarIcons.CHANGE_LAYOUT_MODE)) {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				DayView currentDayView = (DayView) calendarPanel.getCurrentView().getView();
				System.out.println("### layout mode 2 ###");
				currentDayView.setLayoutMode(Layout.DAY_COLUMN_SEPARATED_BY_MAX_NUMBER_OF_CALENDAR);
				try {
					currentDayView.refresh0();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};

		calendarPanel.getFunctionsButtonPanel().addAction(layout2Action);
		///////////////////////////////////////////////////////////////////////////
		
		this.add(calendarPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.pack();
		this.setSize(1000, 700);
		this.setVisible(true);
//		for (int i = 0; i < 50; i++) {
//			this.setSize(850+i*5, 800+i*2);
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}

	}

	@SuppressWarnings("unchecked")
	private synchronized void updateEventsForActiveCalendars() {
		/* ================================================== */
		// add all events from active calendars
		List<Event> allActiveEvents = new ArrayList<Event>();

		for (NamedCalendar nc : calendarPanel.getCalendars()) {
			if (nc.isActive() || nc.isSelected())
				// this is just for demonstration. You can define a time periode to 
				// get events from the calendar
				allActiveEvents.addAll(nc.getEvents(null, null));
		}

		Collections.sort(allActiveEvents);

		eventDataList.clear();
		eventDataList.addAll(allActiveEvents);
		/* ================================================== */
	}

	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		SwingTracing.enableEventDispatcherTimeTracing(1000, false);
//		SwingTracing.enableEventDispatcherThreadViolationTracing(false);
//		SwingTracing.enableRepaintTracing("bizcal");
		
//		Translatrix.setLocale(new Locale("en_US"));
		new CalendarDemo();
	}
	
	
	class DemoCalendarListener implements CalendarListener {

		public void closeCalendar(Object calId) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void copy(List<Event> list) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void dateChanged(Date date) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void dateSelected(Date date) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void deleteEvent(Event event) throws Exception {
			/* ====================================================== */
			eventDataList.remove(event);
			/* ====================================================== */
		}

		public void deleteEvents(List<Event> events) {
			/* ====================================================== */
			eventDataList.removeAll(events);
			/* ====================================================== */
		}

		public void eventClicked(Object id, Event _event, FrameArea area, MouseEvent e) {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void eventDoubleClick(Object id, Event event, MouseEvent mouseEvent) {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void eventSelected(Object id, Event event) throws Exception {
			/* ====================================================== */
			// try to find the calendar by its id
			if (calendarPanel.getCalendars() == null)
				return;
			
			// deselect all calendars, except the current selected calendar of the event
			for (NamedCalendar cal : calendarPanel.getCalendars())
			{
				// deselect calendar first
				cal.setSelected(false);
				// remove border from Calendar						
				cal.removeBorder();
				
				if (cal.getId().equals(event.get(Event.CALENDAR_ID))) {
					
					calendarPanel.setSelectedCalendar(cal);
					cal.setSelected(true);
					// add border to Calendar
					cal.addBorder();
				}
			}
			/* ====================================================== */
		}

		public void eventsSelected(List<Event> list) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void moved(Event event, Object orgCalId, Date orgDate, Object newCalId, Date newDate) throws Exception {
			/* ====================================================== */
			event.move(newDate);
			eventDataList.trigger();
			/* ====================================================== */
		}

		public void newCalendar() throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void newEvent(Object id, Date date) throws Exception {
			/* ====================================================== */
			// create a normal appointment lasting 15 min
			DateInterval interval = new DateInterval(date, new Date(date.getTime()+900000));
			newEvent(id, interval);
			/* ====================================================== */
		}

		public void newEvent(Object id, DateInterval interval) throws Exception {
			/* ====================================================== */

			// create an Event object
//			Event newEvent = appointment2Event(ap);

			NamedCalendar nc = calendarPanel.getSelectedCalendar();
			/* ------------------------------------------------------- */
			if (nc == null)
				return;
			/* ------------------------------------------------------- */
			Event event = new Event();
			event.setStart(interval.getStartDate());
			event.setEnd(interval.getEndDate());
			event.setId(id);
			
			nc.addEvent("clientXXX", event);
			
			/* ====================================================== */
		}

		public void paste(Object calId, Date date) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void resized(Event event, Object orgCalId, Date orgEndDate, Date newEndDate) throws Exception {
			/* ====================================================== */
			NamedCalendar nc = calendarPanel.getSelectedCalendar();
			/* ------------------------------------------------------- */
			if (nc == null)
				return;
			/* ------------------------------------------------------- */
			event.setEnd(newEndDate);

			nc.saveEvent("clientXXX", event, false);
			/* ====================================================== */
		}

		public void selectionReset() throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}

		public void showEvent(Object id, Event event) throws Exception {
			/* ====================================================== */
			// TODO Auto-generated method stub
			/* ====================================================== */
		}
		
	}
	
	
	
	

	/**
	 * @author martin.heinemann@tudor.lu
	 * 27.06.2007
	 * 11:53:49
	 *
	 *
	 * @version
	 * <br>$Log: CalendarDemo.java,v $
	 * <br>Revision 1.14  2012/04/24 13:49:49  thorstenroth
	 * <br>1. Add border to class NamedCalendar to bordered a select Calendar in the calendar panel.
	 * <br>2. Fix Bug: Sometimes if a agenda entry has been selected the calendar of the entry was not selected. Now: The Calendar will be selected if a entry of the calendar is selected.
	 * <br>
	 * <br>Revision 1.13  2011/10/20 15:32:21  thorstenroth
	 * <br>1. add new calendar type the background calendar type which is displayed over a whole column.
	 * <br>2. fix Bug: public holidays are not displayed over the whole daily column
	 * <br>
	 * <br>Revision 1.12  2011/03/04 12:45:35  thorstenroth
	 * <br>1. Improvement of the mouse controls when event gets resize and move in the calendar.
	 * <br>2. Bug Fix: The position of the current timeline is now correct and only shown ar the current day.
	 * <br>3. Bug Fix: Because of the bug the view can not difference between Events form different calendars which have the same start and end time so sometimes by resize or move a event there are side effects when drawing the events.
	 * <br>
	 * <br>Revision 1.11  2011/02/22 14:59:32  thorstenroth
	 * <br>1. Add a new layout for the day view. This layout split the day column into a number of lines which is equal to the number of calendars which are active. The events of one calendar are now shown in one line, one below the other.
	 * <br>
	 * <br>2. Add a new horizontal line to the day view to represent the current time.
	 * <br>
	 * <br>Revision 1.10  2011/02/11 07:22:07  thorstenroth
	 * <br>Add a new view to the calendar the 'Three Day View' which shows three days per interval.
	 * <br>
	 * <br>Revision 1.9  2009/04/28 14:11:26  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.8  2008/06/19 12:20:00  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.7  2008/06/09 14:10:09  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.6  2008/05/30 11:36:47  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.5  2008/04/08 13:17:53  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.4  2008/03/28 08:45:12  heine_
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.3  2008/01/21 14:14:17  heine_
	 * <br>code cleanup and java doc
	 * <br>
	 * <br>Revision 1.17  2007/06/27 14:59:55  heinemann
	 * <br>*** empty log message ***
	 * <br>
	 * <br>Revision 1.16  2007/06/27 11:59:42  heinemann
	 * <br>*** empty log message ***
	 * <br>
	 *   
	 */
	class TestNamedCalendar extends NamedCalendar {
		
		private List<Event> calendarEvents = new ArrayList<Event>();
		
		public TestNamedCalendar(String name, String description, Color color) {
			super(name, description, color);
			
			this.setId(this.hashCode());
		}

		@Override
		public List<Event> getEvents(Date from, Date to) {
			return calendarEvents;
		}

		@Override
		public void deleteEvent(String clientId, Event event) {
			/* ====================================================== */
			calendarEvents.remove(event);
			eventDataList.remove(event);
			/* ====================================================== */
		}

		@Override
		public List<Event> addEvent(String clientId, Event event) {
			/* ====================================================== */
			// set the calendar id of the event
			event.set(Event.CALENDAR_ID, this.getId());
			// set Calendar isBackground to Event
			event.set(Event.CALENDAR_IS_BACKGROUND, this.isBackground());
			event.setColor(this.getColor());
			
			eventDataList.add(event);
			calendarEvents.add(event);
			return null;
			/* ====================================================== */
		}

		@Override
		public List<Event> saveEvent(String clientId, Event event, boolean userInteraction) {
			/* ====================================================== */
			return null;
			/* ====================================================== */
		}
	}
}
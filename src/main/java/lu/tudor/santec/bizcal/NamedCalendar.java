/*******************************************************************************
 * Copyright (c) 2007 by CRP Henri TUDOR - SANTEC LUXEMBOURG 
 * check http://www.santec.tudor.lu for more information
 *  
 * Contributor(s):
 * Johannes Hermen  johannes.hermen(at)tudor.lu                            
 * Martin Heinemann martin.heinemann(at)tudor.lu  
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
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import lu.tudor.santec.bizcal.widgets.CheckBoxPanel;
import bizcal.common.Event;

/**
 * @author martin.heinemann@tudor.lu
 * 21.01.2008
 * 09:08:47
 *
 *
 * @version
 * <br>$Log: NamedCalendar.java,v $
 * <br>Revision 1.5  2012/04/24 13:49:49  thorstenroth
 * <br>1. Add border to class NamedCalendar to bordered a select Calendar in the calendar panel.
 * <br>2. Fix Bug: Sometimes if a agenda entry has been selected the calendar of the entry was not selected. Now: The Calendar will be selected if a entry of the calendar is selected.
 * <br>
 * <br>Revision 1.4  2011/10/20 15:32:21  thorstenroth
 * <br>1. add new calendar type the background calendar type which is displayed over a whole column.
 * <br>2. fix Bug: public holidays are not displayed over the whole daily column
 * <br>
 * <br>Revision 1.3  2008/04/08 13:17:53  heine_
 * <br>*** empty log message ***
 * <br>
 * <br>Revision 1.2  2008/01/21 14:14:17  heine_
 * <br>code cleanup and java doc
 * <br>
 * <br>Revision 1.9  2008-01-21 14:05:26  heinemann
 * <br>code cleanup and java doc
 * <br>
 *   
 */
public abstract class NamedCalendar  {


	public static final String CALENDAR_NAME = "calendar_name";

	private String name;
	private String description;
	private Color color;
	private boolean showing;
	private boolean selected;
	private Integer id;
	private Boolean isBackground;

	private CheckBoxPanel checkBox;
	
	private Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

	public NamedCalendar(String name){
		this.name = name;
		this.isBackground = false;
	}

	public NamedCalendar(String name, String description) {
		this.name = name;
		this.description = description;
		this.isBackground = false;
	}

	public NamedCalendar(String name, String description, Color color) {
		this.name = name;
		this.description = description;
		this.color = color;
		this.isBackground = false;
	}
	
	public NamedCalendar(String name, Boolean isBackground)
	{
		this.name = name;
		this.isBackground = isBackground;
	}
	/**
	 * @return
	 */
	public Integer getId() {
		/* ================================================== */
		return this.id;
		/* ================================================== */
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		/* ================================================== */
		this.id = id;
		/* ================================================== */
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
		if (checkBox != null)
			checkBox.setColor(color);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the showing
	 */
	public boolean isActive() {
		return showing;
	}

	/**
	 * @param showing the showing to set
	 */
	public void setActive(boolean showing) {
		this.showing = showing;
	}

	public String toString() {
		return this.getName() + " (" + this.getDescription() + ") " + this.isActive();
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/* ------------------------------------------------------- */
	/**
	 * @return the checkBox
	 */
	public CheckBoxPanel getCheckBox() {
		return checkBox;
	}

	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(CheckBoxPanel checkBox) {
		this.checkBox = checkBox;
	}

	public void setBackground(Boolean isBackground)
	{
		this.isBackground = isBackground;
	}
	
	public Boolean isBackground()
	{
		return this.isBackground;
	}
	
	public void setBorder(Color color, int thickness)
	{
		this.border = BorderFactory.createLineBorder(color, thickness);
		this.getCheckBox().setBorder(this.border);
	}
	
	public void addBorder()
	{
		this.getCheckBox().setBorder(this.border);
	}
	
	public void removeBorder()
	{
		this.getCheckBox().setBorder(null);
	}
	
	public abstract List<Event> addEvent(String clientId, Event event);

	public abstract void deleteEvent(String clientId, Event event);

	public abstract List<Event> getEvents(Date from, Date to) ;

	/**
	 * Saves an event
	 *
	 * @param event
	 * @param userInteraction false means just saving without user interaction
	 */
	public abstract List<Event> saveEvent(String clientId, Event event, boolean userInteraction);

}

package lu.tudor.santec.bizcal.listeners;

/**
 * Listener interface to be informed of zoom change events
 * 
 * @author martin.heinemann@tudor.lu
 * 11.05.2009
 * 18:18:53
 *
 *
 * @version
 * <br>$Log: IZoomSliderListener.java,v $
 * <br>Revision 1.1  2009/05/11 16:45:44  heine_
 * <br>added a listener for the zoom slider
 * <br>
 *   
 */
public interface IZoomSliderListener {
	
	public void zoomPositionchanged(int value);
}

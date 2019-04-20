package open.dolphin.relay;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import open.dolphin.project.Project;

/**
 *
 * @author kazushi Minagawa
 */
public class PVTRelayProxy implements PropertyChangeListener {
    
    private PropertyChangeListener pcl;
    
    public PVTRelayProxy() {
        //ChartEventHandler ceh = ChartEventHandler.getInstance();
        //ceh.addPropertyChangeListener(getListener());
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        if (getListener()!=null) {
            getListener().propertyChange(pce);
        }
    }
    
    private PropertyChangeListener getListener() {
        
        if (pcl==null) {
            String name = Project.getString(Project.PVT_RELAY_NAME);
            
            if (name!=null && name.toLowerCase().startsWith("fev")) {
                pcl = (PropertyChangeListener)create("open.dolphin.relay.FEV70Relay");
                
            } else {
                // default
                pcl = (PropertyChangeListener)create("open.dolphin.relay.PVTRelay");
            }
        }
        return pcl;
    }
    
    private Object create(String clsName) {
        try {
            return Class.forName(clsName).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException 
                | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }
}

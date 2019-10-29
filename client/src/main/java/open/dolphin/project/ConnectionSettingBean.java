package open.dolphin.project;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Kazushi Minagawa
 */
public final class ConnectionSettingBean extends AbstractSettingBean {
    
    // Hosiptal ID
    private String facilityId;
    
    // User name to login
    private String userId;
    
    // Schema http/https
    private String schema;
    
    // Server address
    private String server;

    // Port number
    private String port;
    
    // Context root
    private String contextRoot;
    
    private final Map<String, String[]> tagMap = new HashMap<>(5, 0.75f);
    
    
    public ConnectionSettingBean() {
        tagMap.put("schema", new String[]{"http", "https"});
        tagMap.put("port", new String[]{"8080", "80", "443", "none"});
        tagMap.put(Project.CONTEXT_ROOT, new String[]{Project.CONTEXT_ROOT_PRO, Project.CONTEXT_ROOT_DOCKER});
    }
    
    @Override
    public String[] propertyOrder() {
       return new String[]{"facilityId", "userId", "schema", "server", "port", "contextRoot"};
    }
    
    @Override
    public boolean isTagProperty(String property) {
        return tagMap.get(property)!=null;
    }
    
    @Override
    public String[] getTags(String property) {
        String[] ret = tagMap.get(property);
        return ret;
    }
    
    @Override
    public boolean isValidBean() {
        boolean valid = facilityId!=null && !"".equals(facilityId);
        valid = valid && (userId!=null && !"".equals(userId));
        valid = valid && (schema!=null && !"".equals(schema));
        valid = valid && (server!=null && !"".equals(server));
        valid = valid && (port!=null && !"".equals(port));
        valid = valid && (contextRoot!=null && !"".equals(contextRoot));
        return valid;
    }
    
    @Override
    public void populate() {
        
        ProjectStub stub = Project.getProjectStub();
        
        setFacilityId(stub.getFacilityId());
        setUserId(stub.getUserId());

        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "serverURI={0}", stub.getServerURI());
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "schema={0}", stub.getSchema());
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "sever={0}", stub.getServer());
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "port={0}", stub.getPort());
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "serverType={0}", stub.getContextRoot());
            
        setSchema(stub.getSchema());
        setServer(stub.getServer());
        setPort(stub.getPort());
        
        setContextRoot(stub.getContextRoot());
    }
    
    @Override
    public void store() {
        
        if (!isValidBean()) {
            return;
        }
        
        ProjectStub stub = Project.getProjectStub();
        
        stub.setFacilityId(getFacilityId());
        stub.setUserId(getUserId());

        // Constract URI
        StringBuilder sb = new StringBuilder();
        sb.append(getSchema()).append("://").append(getServer());
        if (!"none".equals(getPort())) {
            sb.append(":").append(getPort());
        }
        String uri = sb.toString();
        java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.FINE, "serverURI={0}", uri);
        stub.setServerURI(uri);
        
        stub.setContextRoot(getContextRoot());
    }
    
    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

}

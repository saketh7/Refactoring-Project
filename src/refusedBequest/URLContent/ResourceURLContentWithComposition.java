package refusedBequest.URLContent;

import java.net.MalformedURLException;
import java.net.URL;

public class ResourceURLContentWithComposition {
	private URLContent urlContent;
	private boolean multiPartResource;
	  
	  /**
	   * Creates a content for <code>resourceName</code> relative to <code>resourceClass</code>.
	   * @param resourceClass the class relative to the resource name to load
	   * @param resourceName  the name of the resource
	   * @throws IllegalArgumentException if the resource doesn't match a valid resource.
	   */
	  public ResourceURLContentWithComposition(Class<?> resourceClass, 
	                            String resourceName) {
	    this(resourceClass, resourceName, false);
	  }

	  /**
	   * Creates a content for <code>resourceName</code> relative to <code>resourceClass</code>.
	   * @param resourceClass the class relative to the resource name to load
	   * @param resourceName  the name of the resource
	   * @param multiPartResource  if <code>true</code> then the resource is a multi part resource 
	   *           stored in a folder with other required resources
	   * @throws IllegalArgumentException if the resource doesn't match a valid resource.
	   */
	  public ResourceURLContentWithComposition(Class<?> resourceClass,
	                            String resourceName, 
	                            boolean multiPartResource) {
	    this.urlContent = new URLContent(getClassResource(resourceClass, resourceName));
	    if (urlContent.getURL() == null) {
	      throw new IllegalArgumentException("Unknown resource " + resourceName);
	    }
	    this.multiPartResource = multiPartResource;
	  }
	  
	  /**
	   * Creates a content for <code>resourceName</code> relative to <code>resourceClassLoader</code>.
	   * <code>resourceName</code> is absolute and shouldn't start with a slash.
	   * @param resourceClassLoader the class loader used to load the given resource name
	   * @param resourceName  the name of the resource
	   * @throws IllegalArgumentException if the resource doesn't match a valid resource.
	   */
	  public ResourceURLContentWithComposition(ClassLoader resourceClassLoader, 
	                            String resourceName) {
	    this.urlContent = new URLContent(resourceClassLoader.getResource(resourceName));
	    if (urlContent.getURL() == null) {
	      throw new IllegalArgumentException("Unknown resource " + resourceName);
	    }
	  }

	  private static final boolean isJava1dot5dot0_16 = 
	      System.getProperty("java.version").startsWith("1.5.0_16"); 
	  
	  /**
	   * Returns the URL of the given resource relative to <code>resourceClass</code>.
	   */
	  private static URL getClassResource(Class<?> resourceClass,
	                                      String resourceName) {
	    URL defaultUrl = resourceClass.getResource(resourceName);
	    // Fix for bug #6746185
	    // http://bugs.sun.com/view_bug.do?bug_id=6746185
	    if (isJava1dot5dot0_16
	        && defaultUrl != null
	        && "jar".equalsIgnoreCase(defaultUrl.getProtocol())) {
	      String defaultUrlExternalForm = defaultUrl.toExternalForm();
	      if (defaultUrl.toExternalForm().indexOf("!/") == -1) {
	        String fixedUrl = "jar:" 
	          + resourceClass.getProtectionDomain().getCodeSource().getLocation().toExternalForm() 
	          + "!/" + defaultUrl.getPath();
	        
	        if (!fixedUrl.equals(defaultUrlExternalForm)) {
	          try {
	            return new URL(fixedUrl);
	          } catch (MalformedURLException ex) {
	            // Too bad: keep defaultUrl
	          } 
	        }
	      }
	    }
	    return defaultUrl;
	  }

	  /**
	   * Creates a content for <code>resourceUrl</code>. 
	   * @param url  the URL of the resource
	   */
	  public ResourceURLContentWithComposition(URL url, boolean multiPartResource) {
	    this.urlContent = new URLContent(url);
	    this.multiPartResource = multiPartResource;
	  }

	  /**
	   * Returns <code>true</code> if the resource is a multi part resource stored 
	   * in a folder with other required resources.
	   */
	  public boolean isMultiPartResource() {
	    return this.multiPartResource;
	  }
}

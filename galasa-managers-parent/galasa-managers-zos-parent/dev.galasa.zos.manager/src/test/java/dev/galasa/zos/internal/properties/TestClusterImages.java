/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2020.
 */
package dev.galasa.zos.internal.properties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import dev.galasa.framework.spi.ConfigurationPropertyStoreException;
import dev.galasa.framework.spi.IConfigurationPropertyStoreService;
import dev.galasa.framework.spi.cps.CpsProperties;
import dev.galasa.zos.ZosManagerException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ZosPropertiesSingleton.class, CpsProperties.class})
public class TestClusterImages {
    
    @Mock
    private IConfigurationPropertyStoreService configurationPropertyStoreServiceMock;
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    
    private static final String CLUSTER_ID = "cluster";
    
    private static final List<String> CLUSTERS = Arrays.asList(new String[] {"image1", "image2"});
    
    @Test
    public void testConstructor() {
        ClusterImages clusterImages = new ClusterImages();
        Assert.assertNotNull("Object was not created", clusterImages);
    }
    
    @Test
    public void testNull() throws Exception {
        exceptionRule.expect(ZosManagerException.class);
        exceptionRule.expectMessage("Unable to locate zOS images for cluster " + null + ", see property zos.cluster.*.images");
        
        getProperty(null, Collections.emptyList());
    }
    
    @Test
    public void testValid() throws Exception {
        Assert.assertEquals("Unexpected value returned from ClusterImages.get()", CLUSTERS, getProperty(CLUSTER_ID, CLUSTERS));
    }
    
    @Test
    public void testException() throws Exception {
        exceptionRule.expect(ZosManagerException.class);
        exceptionRule.expectMessage("Problem asking the CPS for the cluster images for cluster 'ANY'");
        
        getProperty("ANY", null, true);
    }

    private List<String> getProperty(String arg, List<String> value) throws Exception {
        return getProperty(arg, value, false);
    }
    
    private List<String> getProperty(String arg, List<String> value, boolean exception) throws Exception {
        PowerMockito.spy(ZosPropertiesSingleton.class);
        PowerMockito.doReturn(configurationPropertyStoreServiceMock).when(ZosPropertiesSingleton.class, "cps");
        PowerMockito.spy(CpsProperties.class);
        
        if (!exception) {
            PowerMockito.doReturn(value).when(CpsProperties.class, "getStringList", Mockito.any(), Mockito.anyString(), Mockito.anyString());
        } else {
            PowerMockito.doThrow(new ConfigurationPropertyStoreException()).when(CpsProperties.class, "getStringList", Mockito.any(), Mockito.anyString(), Mockito.anyString());
        }
        
        return ClusterImages.get(arg);
    }
}

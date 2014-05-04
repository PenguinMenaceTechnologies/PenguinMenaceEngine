/**
 *
 */
package net.pme;

import net.pme.network.NetworkInitializer;
import net.pme.network.RegisteredPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Setup a default network client.
 *
 * @author Michael Fürst
 * @version 1.0
 */
public class TestNetworkInitializer implements NetworkInitializer {

    /* (non-Javadoc)
     * @see net.pme.network.NetworkInitializer#getRegisteredPackets()
     */
    @Override
    public List<RegisteredPacket> getRegisteredPackets() {
        return new ArrayList<RegisteredPacket>();
    }

    /* (non-Javadoc)
     * @see net.pme.network.NetworkInitializer#getHost()
     */
    @Override
    public String getHost() {
        return "localhost";
    }

    /* (non-Javadoc)
     * @see net.pme.network.NetworkInitializer#getPort()
     */
    @Override
    public int getPort() {
        return 25545;
    }

    /* (non-Javadoc)
     * @see net.pme.network.NetworkInitializer#isServer()
     */
    @Override
    public boolean isServer() {
        return true;
    }

}

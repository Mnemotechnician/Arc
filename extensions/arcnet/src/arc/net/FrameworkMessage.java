package arc.net;

/**
 * Marker interface to denote that a message is used by the Ninja framework and
 * is generally invisible to the developer.
 * @author Nathan Sweet <misc@n4te.com>
 */
public interface FrameworkMessage{
    FrameworkMessage.KeepAlive keepAlive = new KeepAlive();

    /**
     * Internal message to give the client the server assigned connection ID.
     */
    class RegisterTCP implements FrameworkMessage{
        public int connectionID;
    }

    /**
     * Internal message to give the server the client's UDP port.
     */
    class RegisterUDP implements FrameworkMessage{
        public int connectionID;
    }

    /**
     * Internal message to keep connections alive.
     */
    class KeepAlive implements FrameworkMessage{
    }

    /**
     * Internal message to discover running servers.
     */
    class DiscoverHost implements FrameworkMessage{
    }

    /**
     * Internal message to determine round trip time.
     */
    class Ping implements FrameworkMessage{
        public int id;
        public boolean isReply;
    }
}

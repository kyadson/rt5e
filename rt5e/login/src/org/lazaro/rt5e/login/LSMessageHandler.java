package org.lazaro.rt5e.login;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.lazaro.rt5e.LoginApp;
import org.lazaro.rt5e.logic.login.LoginResponse;
import org.lazaro.rt5e.logic.player.PlayerDefinition;
import org.lazaro.rt5e.network.Connection;
import org.lazaro.rt5e.network.ConnectionMap;
import org.lazaro.rt5e.network.Packet;
import org.lazaro.rt5e.network.PacketBuilder;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.util.Map;

/**
 * @author Lazaro
 */
@ChannelPipelineCoverage("all")
public class LSMessageHandler extends SimpleChannelHandler {
    /**
     * Generate world list data.
     * <p/>
     * For revision: 594 - 597
     *
     * @param world    The world the user is on.
     * @param username
     */
    private void generateWorldListData(WorldSession world, String username) {
        PacketBuilder mb = new PacketBuilder(3, Packet.Type.VAR_BYTE);
        mb.putString(username);

        mb.putByte(1).putByte(2).putByte(1);

        mb.putSmart(LoginApp.getGameWorlds().size()); // world count
        for (Map.Entry<Integer, WorldSession> entry : LoginApp.getGameWorlds().entrySet()) {
            mb.putSmart(0).putGJString2("USA"); // country id and name?
        }

        mb.putSmart(0).putSmart(LoginApp.getGameWorlds().size() + 1).putSmart(LoginApp.getGameWorlds().size()); // world count
        for (Map.Entry<Integer, WorldSession> entry : LoginApp.getGameWorlds().entrySet()) {
            mb.putSmart(entry.getKey()); // world id
            mb.putByte(0); // loc?
            mb.putInt(0); // flag?
            mb.putGJString2("World " + entry.getKey()); // activity
            mb.putGJString2(((InetSocketAddress) entry.getValue().getConnection().getChannel().getRemoteAddress()).getHostName()); // world ip
        }
        mb.putInt(0x94DA4A87); // magic key

        for (Map.Entry<Integer, WorldSession> entry : LoginApp.getGameWorlds().entrySet()) {
            mb.putSmart(entry.getKey()); // world id
            mb.putShort(entry.getValue().getPlayers().size()); // player count
        }

        world.getConnection().write(mb.toPacket());
    }

    private void loadPlayer(WorldSession world, String username, String password, int type) {
        LoginResponse resp = LoginResponse.ERROR;
        PlayerDefinition player = null;
        if (LoginApp.getPlayers().containsKey(username)) {
            resp = LoginResponse.ALREADY_ONLINE;
        } else {
            try {
                ResultSet rs = LoginApp.getSQLHandler()
                        .getConnection().createStatement().executeQuery(
                                "SELECT * FROM saved_games WHERE username='"
                                        + username + "' LIMIT 1");
                do {
                    if (rs.next()) {
                        String savedPassword = rs.getString("password");
                        if (!savedPassword.equals(password)) {
                            resp = LoginResponse.INVALID_DETAILS;
                            break;
                        }
                        player = new PlayerDefinition();
                        player.setCoordX(rs.getInt("coord_x"));
                        player.setCoordY(rs.getInt("coord_y"));
                        player.setCoordZ(rs.getInt("coord_z"));
                        resp = LoginResponse.LOGIN;
                    } else {
                        // result = LoginProcessor.INVALID_DETAILS; // If you
                        // want people to register some how
                        // OR
                        LoginApp.getSQLHandler()
                                .getConnection().createStatement()
                                .executeUpdate(
                                        "INSERT INTO saved_games (username, password) VALUES ('"
                                                + username + "', '" + password
                                                + "')"); // If you want to
                        // automatically
                        // create an account
                        // like most private
                        // servers do.
                        loadPlayer(world, username, password, type);
                        return;
                    }
                } while (false);
            } catch (Exception e) {
                System.err.println("Error loading character [name=" + username + "]");
                e.printStackTrace();
                resp = LoginResponse.ERROR;
            }
        }

        PacketBuilder mb = new PacketBuilder(2, Packet.Type.VAR_BYTE);
        mb.putString(username).putByte(resp.getResponseCode());
        if (resp == LoginResponse.LOGIN) {
            mb.putObject(player);
            if (type != 19) {
                LoginApp.getPlayers().put(username, world);
                world.getPlayers().add(username);

                System.out.println("Registered player [name=" + username + ", world="
                        + world.getId() + "]");
            }
        }
        world.getConnection().write(mb.toPacket());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (e.getMessage() instanceof Packet) {
            Connection conn = ConnectionMap.forChannel(ctx.getChannel());
            Packet packet = (Packet) e.getMessage();

            if (conn.getAttachment() != null) {
                WorldSession world = (WorldSession) conn.getAttachment();
                String username = packet.getString();
                switch (packet.getOpcode()) {
                    case 1: // check if player is online
                        PacketBuilder smf = new PacketBuilder(1, Packet.Type.VAR_BYTE).putString(username).putByte(LoginApp.getPlayers()
                                .containsKey(username) ? 1 : 0);
                        conn.write(smf.toPacket());
                        break;
                    case 2: // load player
                        int type = packet.getUnsigned();
                        String password = packet.getString();
                        loadPlayer(world, username, password, type);
                        break;
                    case 3: // save player
                        type = packet.getUnsigned();
                        PlayerDefinition player = (PlayerDefinition) packet.getObject();
                        savePlayer(world, username, type, player);
                        break;
                    case 4: // generate world list data
                        generateWorldListData(world, username);
                        break;
                }
            }
        }
    }

    private void savePlayer(WorldSession world, String username, int type, PlayerDefinition player) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE saved_games SET ");
        query.append("coord_x='").append(player.getCoordX()).append("'");
        query.append(", coord_y='").append(player.getCoordY()).append("'");
        query.append(", coord_z='").append(player.getCoordZ()).append("'");
        query.append(" WHERE username='").append(username).append("'");
        try {
            LoginApp.getSQLHandler().getConnection()
                    .createStatement().executeUpdate(query.toString());
        } catch (Exception e) {
            System.err.println("Unable to save player : " + username
                    + "!");
            e.printStackTrace();
        }

        if (type != 19) {
            LoginApp.getPlayers().remove(username);
            world.getPlayers().remove(username);

            System.out.println("Un-registered player [name=" + username + ", world="
                    + world.getId() + "]");
        }
    }
}

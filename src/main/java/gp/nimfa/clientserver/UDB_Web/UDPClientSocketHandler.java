package gp.nimfa.clientserver.UDB_Web;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;

@Component
public class UDPClientSocketHandler
{

    //private DatagramSocket socket;
    private static DatagramSocket socket;

    static
    {
        try
        {
            socket = new DatagramSocket();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private InetAddress serverAddress;
    private int serverPort;


    public void connect(String ipAddress, int port) throws IOException
    {
        //socket = new DatagramSocket();
        serverAddress = InetAddress.getByName(ipAddress);
        serverPort = port;
    }

    public void disconnect()
    {
        if (socket != null)
        {
            socket.close();
        }
    }

    public void sendData(String data) throws IOException
    {
        byte[] sendData = hexStringToByteArray(data);
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        socket.send(sendPacket);
    }

    //Попытка 1:
    public String receiveData() throws IOException
    {
        socket.setSoTimeout(500);
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, serverAddress, serverPort);
        String response = "Нет ответа :(";
        try
        {
            socket.receive(receivePacket);
            response = new String(receivePacket.getData(), 0, receivePacket.getLength());
        }
        catch (SocketTimeoutException e)
        {
            return response;
        }
        return response;
    }

    private static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public DatagramSocket getSocket()
    {
        return socket;
    }

    public InetAddress getServerAddress()
    {
        return serverAddress;
    }

    public int getServerPort()
    {
        return serverPort;
    }
}

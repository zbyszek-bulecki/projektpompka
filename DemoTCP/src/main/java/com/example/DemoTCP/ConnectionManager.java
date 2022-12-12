package com.example.DemoTCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Set;


public class ConnectionManager {

    private final static short PING_OPCODE = 0;
    private final static short PONG_OPCODE = 1;
    private final static short MSG_OPCODE = 2;

    private final static int HEADER_SIZE = 3;
    private final static int HEART_BIT_SECONDS = 5;
    private final static int PONG_TIMEOUT = 2;

    private LocalDateTime lastBit;//ostania wiadomość lub ostatni pong
    private LocalDateTime lastPing;
    private TcpConnectionThreadPool tcpConnectionThreadPool;
    private MessageHandler messageHandler;
    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;
    private byte[] remainingData = new byte[]{};


    public ConnectionManager(Socket clientSocket, MessageHandler messageHandler) throws IOException {
        this.clientSocket = clientSocket;
        this.messageHandler = messageHandler;
        out = clientSocket.getOutputStream();
        in = clientSocket.getInputStream();
    }

    public void start(TcpConnectionThreadPool tcpConnectionThreadPool){
        this.tcpConnectionThreadPool = tcpConnectionThreadPool;
        System.out.println("connected " + this);
        lastBit = LocalDateTime.now();
        tcpConnectionThreadPool.submit(this::read);
    }

    public void stop() {
        try {
            in.close();
            out.flush();
            out.close();
            clientSocket.close();
        }
        catch (IOException exception){
            System.out.println("Connection didn't clos properly.");
        }
    }

    private void read() {
        try {
            byte[] data = readDataToBuffer();
            if(data.length>0){
                readMessage(data);
            }
            tcpConnectionThreadPool.submit(this::read);
        } catch (IOException e) {
            System.out.println("disconnected " + this);
            stop();
            throw new RuntimeException(e);
        }
    }

    private byte[] readDataToBuffer() throws IOException {
        if(lastPing!=null && checkTimeOut(lastPing, LocalDateTime.now(), PONG_TIMEOUT)){
            throw new IOException();
        }

        if(lastPing==null && checkTimeOut(lastBit, LocalDateTime.now(), HEART_BIT_SECONDS)){
            sendPing();
            return new byte[0];
        }

        if(clientSocket.isInputShutdown() || clientSocket.isClosed()){
            throw new IOException();
        }

        if(in.available()>0) {
            byte[] buffer = new byte[8000];
            int size = in.read(buffer);
            byte[] data = Arrays.copyOfRange(buffer, 0, size);

            if (size < 0) {
                throw new IOException();
            } else if (size > 0) {
                return concatArrays(remainingData, data);
            }
        }
        return new byte[0];
    }

    private void readMessage(byte[] data) throws IOException {
        if(data.length<3){ //TODO czy to ma sens?
            remainingData = data;
            tcpConnectionThreadPool.submit(this::read);
            return;
        }

        Header header = parseHeader(data);

        if(Set.of(PING_OPCODE,PONG_OPCODE).contains(header.opcode)){
            if(header.payloadSize>0){
                throw new IOException();
            }
            if(header.opcode==PING_OPCODE){
                sendPong();
            }
        }

        if(data.length < header.payloadSize() + HEADER_SIZE){
            remainingData = data;
        }
        else{
            String payload = obtainPayload(header, data);
            stashRemains(data, header.payloadSize() + HEADER_SIZE);
            lastBit = LocalDateTime.now();
            messageHandler.handle(payload, this);
        }
    }

    private Header parseHeader(byte[] header){
        byte finMask = (byte) 128;
        byte opcodeMask = (byte) 15;
        boolean fin = (finMask & header[0]) == finMask;
        short opcode = (short) (opcodeMask & header[0]);
        int payloadSize = 0;
        for(int i=1; i<HEADER_SIZE; i++){
            payloadSize += Byte.toUnsignedInt(header[i]) << ((HEADER_SIZE - i - 1) * 8);
        }
        return new Header(fin, opcode, payloadSize);
    }

    private String obtainPayload(Header header, byte[] data){
        byte[] message = Arrays.copyOfRange(data, HEADER_SIZE, HEADER_SIZE+header.payloadSize);
        return new String(message, StandardCharsets.UTF_8);
    }

    private void stashRemains(byte[] data, int endOfMessage){
        if(data.length > endOfMessage){
            remainingData = Arrays.copyOfRange(data, endOfMessage, data.length);
        }
    }

    public byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public void sendMessage(String key, String value) throws IOException {
        System.out.println("send msg");
        byte[] header = buildHeader(true, MSG_OPCODE, key.length() + 1 + value.length());
        String payload = key + ":" + value;
        byte[] frame = concatArrays(header, payload.getBytes(StandardCharsets.UTF_8));
        sendFrame(frame);
    }

    private byte[] buildHeader(boolean fin, short opcode, int payloadSize){
        byte[] header = new byte[HEADER_SIZE];
        header[0] = (byte)0;
        if(fin){
            header[0] = (byte)128;
        }
        header[0] = (byte) (header[0] | (byte)opcode);

        for(int i=0; i<HEADER_SIZE-1; i++){
            header[i+1] = (byte) (payloadSize >> (i * 8));
        }
        return header;
    }

    private void sendPing() throws IOException {
        System.out.println("send ping");
        byte[] frame = buildHeader(true, PING_OPCODE, 0);
        sendFrame(frame);
        lastPing = LocalDateTime.now();
    }

    private void sendPong() throws IOException {
        System.out.println("send pong");
        byte[] frame = buildHeader(true, PONG_OPCODE, 0);
        sendFrame(frame);
        lastPing = null;
    }

    private void sendFrame(byte[] frame) throws IOException {
        out.write(frame);
        lastBit = LocalDateTime.now();
    }

    private boolean checkTimeOut(LocalDateTime from, LocalDateTime to, long timeout){
        return ChronoUnit.SECONDS.between(from, to)>timeout;
    }

    public record Header(
            boolean fin,
            short opcode,
            int payloadSize
    ){}


}

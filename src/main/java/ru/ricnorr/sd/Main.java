package ru.ricnorr.sd;


import ru.ricnorr.sd.server.Server;

public class Main {
    public static void main(String[] args) {
        new Server().start(8080);
    }
}
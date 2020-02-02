package com.haeyum.safecorona.models;

public class InfectedRoute {
    private String date;
    private String route;

//    public InfectedRoute(String date, String route) {
//        this.date = date;
//        this.route = route;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}

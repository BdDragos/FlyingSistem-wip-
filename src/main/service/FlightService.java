package service;

import model.Flight;
import observer.Observable;
import observer.Observer;
import repository.FlightRepo;

import java.util.ArrayList;
import java.util.List;
public class FlightService implements Observable<Flight>
{
    private FlightRepo repo;
    private List <Observer<Flight>> observers = new ArrayList<Observer<Flight>>();

    public FlightService(FlightRepo frep)
    {
        this.repo = frep;
    }

    public List<Flight> getAllFlights()
    {
        List<Flight> s = new ArrayList<Flight>();
        List<Flight> list=repo.getAll();
        for (Flight f:list)
            s.add(f);
        notifyObservers();
        return s;

    }

    public int buyFlight(int idf, String client,int noticket,String address)
    {
        Flight cop = repo.findById(idf);
        int nrorig = cop.getFreeseats();
        if (nrorig < noticket)
        {
            return 1;
        }

        repo.updateFlight(cop,client,noticket,address);
        cop = repo.findById(idf);

        if (cop.getFreeseats() == nrorig - noticket)
        {
            notifyObservers();
            return 1;
        }
        else
            return 0;

    }

    public void deleteFlight(Flight c)
    {
        repo.deleteFlight(c.getFlightId());
        if (repo.findById(c.getFlightId()) == null)
            notifyObservers();

    }
    public List<Flight> findByDestinationAndDate(String dest, java.sql.Date dat)
    {
        List<Flight> rez;
        rez = repo.findByDestinationAndDate(dest,dat);
        if (rez != null)
            notifyObservers();
        return rez;
    }

    public List<Flight> findByDestination(String dest)
    {
        List<Flight> rez;
        rez = repo.findByDestination(dest);
        if (rez != null)
            notifyObservers();
        return rez;
    }

    public List<Flight> findByDate(java.sql.Date dat)
    {
        List<Flight> rez;
        rez = repo.findByDate(dat);
        if (rez != null)
            notifyObservers();
        return rez;
    }


    public void addObserver(Observer<Flight> o)
    {
        observers.add(o);
    }

    public void removeObserver(Observer<Flight> o)
    {
        observers.remove(o);
    }

    public void notifyObservers()
    {
        for(Observer<Flight> o : observers)
        {
            o.update(this);
        }
    }
}

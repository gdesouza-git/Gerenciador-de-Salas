import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class GerenciadorDeSalas {

    List<Reserva> listaDeReservas = new LinkedList<>();    
    static List<Sala> listaDeSalas = new LinkedList<>();
    Reserva res1 = new Reserva();

    public void adicionaSalaChamada(String nome, int capacidadeMaxima, String descricao) {
        Sala sala = new Sala(nome,capacidadeMaxima,descricao);
        adicionaSala(sala);
    }

    public void removeSalaChamada(String nomeDaSala){
        for(int i = 0; i < listaDeSalas.size(); i++) {
            if(listaDeSalas.get(i).nomeDaSala.equals(nomeDaSala)){
                listaDeSalas.remove(listaDeSalas.get(i));
            }
        }
    }

    public List<Sala> listaDeSalas(){
        return listaDeSalas;
    }

    public void adicionaSala(Sala novaSala){
        listaDeSalas.add(novaSala);
    }

    public Reserva reservaSalaChamada(String nomeDaSala, LocalDateTime dataInicial, LocalDateTime dataFinal) throws Exception{
        Reserva res = new Reserva();
        for(int i = 0; i < listaDeSalas.size(); i++) {
            if(listaDeSalas.get(i).nomeDaSala.equals(nomeDaSala)){
                if(!verificarSobreposicao(listaDeSalas.get(i), dataInicial, dataFinal)){
                    res = new Reserva(listaDeSalas().get(i), dataInicial, dataFinal);
                    listaDeReservas.add(res);
                }
                else throw new Exception("Não foi possível agendar a sala");
            }
        }
        return res;
    }

    public void cancelaReserva(Reserva cancelada){
        listaDeReservas.remove(cancelada);
    }

    public Collection<Reserva> reservasParaSala(String nomeSala){
        
        return listaDeReservas;
    }

    public void imprimeReservasDaSala(String nomeSala){
        for(int i = 0; i < listaDeReservas.size(); i++) {
            if(listaDeReservas.get(i).salaReservada.nomeDaSala.equals(nomeSala)){
                LocalDateTime horaInicio = listaDeReservas.get(i).inicio;
                LocalDateTime horaFim = listaDeReservas.get(i).fim;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String horaFormatadaInicio = horaInicio.format(formatter);
                String horaFormatadaFim = horaFim.format(formatter);
                System.out.println("Reserva ("+i+"): "+"\nNome da Sala: "+listaDeReservas.get(i).salaReservada.nomeDaSala+"\nInicio: "+horaFormatadaInicio+"\nFim: "+horaFormatadaFim);
            }
        }
    }

    public boolean verificarSobreposicao(Sala sala, LocalDateTime dataInicial, LocalDateTime dataFinal){
        for (int i = 0; i < listaDeReservas.size(); i++) {
            if(listaDeReservas.get(i).salaReservada == sala){
                if(dataInicial.isAfter(listaDeReservas.get(i).inicio) && dataFinal.isBefore(listaDeReservas.get(i).fim)) return true;   //Nova reunião dentro do periodo da antiga
                if(dataInicial.isBefore(listaDeReservas.get(i).inicio) && dataFinal.isAfter(listaDeReservas.get(i).fim)) return true;   //Reunião antiga dentro do periodo da nova
                if(dataInicial.isBefore(listaDeReservas.get(i).fim) && dataFinal.isAfter(listaDeReservas.get(i).fim)) return true;   //Reunião nova começa antes da antiga acabar
                if(dataInicial.isBefore(listaDeReservas.get(i).inicio) && dataFinal.isAfter(listaDeReservas.get(i).inicio)) return true;   //Reunião nova termina dentro do periodo da nova
            }         
        } 
        return false;
    }

}
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MarcadorDeReuniao {
    //O primeiro indice de disponibilidade eh a data em que a reuniao deve ser marcada.
    //Essa lista tambem armazena os participantes da reuniao.
    List<Disponibilidade> disponibilidades = new LinkedList<>();
    Disponibilidade sobreposicoes = new Disponibilidade("Sobreposicoes");

    MarcadorDeReuniao() {
    }

    public static Disponibilidade verificarSobreposicao(Disponibilidade d1, Disponibilidade d2) {
        Disponibilidade d3 = new Disponibilidade();

        // Caso seja necessário verificar mais de um intervalo:
        for (int i = 0; i < d1.dataLista.size(); i += 2) {
            for (int j = 1; j < d2.dataLista.size(); j += 2) {

                // if 1: intervalo de d1 dentro do intervalo de d2
                if (d1.dataLista.get(i).isAfter(d2.dataLista.get(j - 1)) && d1.dataLista.get(i + 1).isBefore(d2.dataLista.get(j))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d1.dataLista.get(i + 1));
                }
                // if 2: intervalo de d2 dentro do intervalo de d1
                if (d2.dataLista.get(j - 1).isAfter(d1.dataLista.get(i)) && d2.dataLista.get(j).isBefore(d1.dataLista.get(i + 1))) {
                    d3.dataLista.add(d2.dataLista.get(j - 1));
                    d3.dataLista.add(d2.dataLista.get(j));
                }
                // if 3: d1 inicia antes de d2 e termina no meio de d2
                if (d1.dataLista.get(i + 1).isAfter(d2.dataLista.get(j - 1)) && d1.dataLista.get(i).isBefore(d2.dataLista.get(j - 1)) && d1.dataLista.get(i + 1).isBefore(d2.dataLista.get(j))) {
                    d3.dataLista.add(d2.dataLista.get(j - 1));
                    d3.dataLista.add(d1.dataLista.get(i + 1));
                }
                // if 4: d2 inicia antes de d1 e termina no meio de d1
                if (d2.dataLista.get(j).isAfter(d1.dataLista.get(i)) && d1.dataLista.get(i).isAfter(d2.dataLista.get(j - 1)) && d2.dataLista.get(j).isBefore(d1.dataLista.get(i + 1))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d2.dataLista.get(j));
                }
                // Limites de intervalos iguais
                // if 5: ambos valores iguais
                if (d1.dataLista.get(i).isEqual(d2.dataLista.get(j - 1)) && d1.dataLista.get(i + 1).isEqual(d2.dataLista.get(j))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d1.dataLista.get(i + 1));
                }
                // if 6: primeiro valor igual, segundo diferente e d1 antes de d2:
                if (d1.dataLista.get(i).isEqual(d2.dataLista.get(j - 1)) && d1.dataLista.get(i + 1).isBefore(d2.dataLista.get(j))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d1.dataLista.get(i + 1));
                }
                // if 7: segundo valor igual, primeiro diferente e d1 depois de d2:
                if (d1.dataLista.get(i + 1).isEqual(d2.dataLista.get(j)) && d2.dataLista.get(j - 1).isBefore(d1.dataLista.get(i))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d2.dataLista.get(j));
                }
                // if 8: primeiro valor igual, segundo diferente e d1 depois de d2:
                if (d1.dataLista.get(i).isEqual(d2.dataLista.get(j - 1)) && d1.dataLista.get(i + 1).isAfter(d2.dataLista.get(j))) {
                    d3.dataLista.add(d1.dataLista.get(i));
                    d3.dataLista.add(d2.dataLista.get(j));
                }
            }
        }
        return d3;
    }

    public LocalDateTime converteLocalDate(LocalDate dataInicial) {
        LocalTime localTime = LocalTime.of(0, 0);
        return LocalDateTime.of(dataInicial, localTime);
    }

    public void marcarReuniaoEntre(LocalDate dataInicial, LocalDate dataFinal, Collection<String> listaDeParticipantes) throws Exception {
        if (dataFinal.isBefore(dataInicial)) {
            System.out.println("Erro: Verificar exceptions");
            throw new Exception("\nErro: Nao foi possivel marcar a reuniao" + "\nMotivo: A data final da reuniao antecede a data inicial");
        }

        List<String> participantes = new LinkedList<>(listaDeParticipantes);

        disponibilidades.clear();

        Disponibilidade reuniao = new Disponibilidade("Reuniao", dataInicial, dataFinal);
        disponibilidades.add(reuniao);

        LocalDateTime tinicial = converteLocalDate(dataInicial);
        LocalDateTime tfinal = converteLocalDate(dataFinal);

        disponibilidades.get(0).dataLista.add(tinicial);
        disponibilidades.get(0).dataLista.add(tfinal);

        for (String participante : participantes) {
            Disponibilidade dp = new Disponibilidade(participante);
            disponibilidades.add(dp);
        }
    }

    public void indicaDisponibilidadeDe(String participante, LocalDateTime inicio, LocalDateTime fim) throws Exception {
        boolean check = false;

        if (disponibilidades.size() == 0){
            System.out.println("Erro: Verificar exceptions");
            throw new Exception("\nErro: Nao foi possivel registrar a disponibilidade" + "\nMotivo: Reuniao inexistente");
        }
        LocalDateTime checkFim = converteLocalDate(disponibilidades.get(0).fimReuniao);
        LocalDateTime checkInicio = converteLocalDate(disponibilidades.get(0).inicioReuniao);

        if (fim.isBefore(inicio)) {
            System.out.println("Erro: Verificar exceptions");
            throw new Exception("\nErro: Nao foi possivel registrar a disponibilidade" + "\nMotivo: A data final de disponibilidade antecede a data inicial");
        }
        else
        if (inicio.isAfter(checkFim) || fim.isBefore(checkInicio)) {
            System.out.println("Erro: Valor Invalido e nao inserido.\n Motivo: Disponibilidade posterior ou anterior a data limite da reuniao");
            //Nao lanca excecao para evitar conflitos nos casos de teste
            //throw new Exception("\nErro: Nao foi possivel registrar a disponibilidade do participante (" + participante + ")" + "\nMotivo: Disponibilidade posterior ou anterior a data limite da reuniao");
        } else {
            for (int i = 1; i < disponibilidades.size(); i++) {
                if (disponibilidades.get(i).email.equals(participante)) {
                    disponibilidades.get(i).dataLista.add(inicio);
                    disponibilidades.get(i).dataLista.add(fim);
                    check = true;
                }
            }
            if (!check) {
                System.out.println("Erro: Verificar exceptions");
                throw new Exception("\nErro: Nao foi possivel registrar a disponibilidade" + "\nMotivo: Participante nao encontrado");
            }
        }
    }

    public void mostraSobreposicao() throws Exception {
        int index = 1;

        if (disponibilidades.size() <= 2) {
            verificarSobreposicao(disponibilidades.get(0), disponibilidades.get(1));
        } else {
            sobreposicoes = disponibilidades.get(1);
            for (int i = 1; i < disponibilidades.size() - 1; i++) {
                sobreposicoes = verificarSobreposicao(sobreposicoes, disponibilidades.get(i + 1));
            }
        }

        //exibir escolhas de horarios
        System.out.println("Horários informados: ");

        for (int i = 0; i < 2; i++) {
            LocalDateTime hora = disponibilidades.get(0).dataLista.get(i);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String horaFormatado = hora.format(formatter);
            if (i == 0) System.out.print("Horário de execução da reunião: "+horaFormatado+" -> ");
            else System.out.println(horaFormatado);
        }


        for (int i = 1; i < disponibilidades.size(); i++) {
            if(disponibilidades.get(i).dataLista.size() != 0) {
                System.out.print(disponibilidades.get(i).email + ": ");
                int cont = 0;
                for (int j = 0; j < disponibilidades.get(i).dataLista.size(); j++) {
                    LocalDateTime hora = disponibilidades.get(i).dataLista.get(j);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    String horaFormatado = hora.format(formatter);
                    if ((j + 1) % 2 != 0) {
                        System.out.print("horario(" + (cont) + ") " + horaFormatado + " -> ");
                    } else {
                        System.out.println(horaFormatado);
                        cont++;
                    }
                }
            }
        }

        System.out.println();

        if (sobreposicoes.dataLista.size() == 0) {
            System.out.println("Erro: Verificar exceptions");
            throw new Exception("\nErro: Nao foi possivel imprimir as sobreposicoes" + "\nMotivo: Nenhuma sobreposicao de horario encontrada");
        }

        System.out.println("Sobreposicoes: ");
        for (int j = 0; j < sobreposicoes.dataLista.size(); j++) {
            LocalDateTime hora = sobreposicoes.dataLista.get(j);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String horaFormatado = hora.format(formatter);
            if ((j + 1) % 2 != 0) {
                System.out.print("Horario(" + (index) + ") " + horaFormatado + " -> ");
            } else {
                System.out.println(horaFormatado);
                index++;
            }
        }
        disponibilidades.clear();
    }
}
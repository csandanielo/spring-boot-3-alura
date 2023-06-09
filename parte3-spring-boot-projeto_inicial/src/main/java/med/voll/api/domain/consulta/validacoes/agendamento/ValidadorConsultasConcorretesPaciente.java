package med.voll.api.domain.consulta.validacoes.agendamento;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorConsultasConcorretesPaciente implements ValidadorAgendamentoDeConsulta {
    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){
        var primeiroHorario = dados.data().withHour(7);
        var ultimoHorario = dados.data().withHour(18);
        var consultaConcorrentePaciente = repository.existsByPacienteIdAndDataBetween
                (dados.idPaciente(), primeiroHorario, ultimoHorario);
        if (consultaConcorrentePaciente){
            throw new ValidacaoException("Paciente possui consulta concorrente neste dia e horario");
        }
    }
}

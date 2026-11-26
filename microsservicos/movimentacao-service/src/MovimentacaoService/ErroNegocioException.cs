namespace MovimentacaoService;

// Erro de regra de negócio já com o status HTTP que deve chegar ao cliente.
public class ErroNegocioException(int status, string mensagem) : Exception(mensagem)
{
    public int Status { get; } = status;
}

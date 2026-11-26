using Microsoft.EntityFrameworkCore;
using MovimentacaoService.Models;

namespace MovimentacaoService.Data;

// Banco Operacional, compartilhado com o reservas-service. O esquema vem de microsservicos/banco-operacional/init.sql.
public class OperacionalDbContext(DbContextOptions<OperacionalDbContext> options) : DbContext(options)
{
    public DbSet<Movimentacao> Movimentacoes => Set<Movimentacao>();
    public DbSet<Reserva> Reservas => Set<Reserva>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Movimentacao>().Property(m => m.Status).HasConversion<string>();
        modelBuilder.Entity<Reserva>().Property(r => r.Status).HasConversion<string>();
    }
}

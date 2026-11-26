using Microsoft.EntityFrameworkCore;
using ReservasService.Models;

namespace ReservasService.Data;

// Banco Operacional, compartilhado com o movimentacao-service. O esquema vem de microsservicos/banco-operacional/init.sql.
public class OperacionalDbContext(DbContextOptions<OperacionalDbContext> options) : DbContext(options)
{
    public DbSet<Reserva> Reservas => Set<Reserva>();

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Reserva>().Property(r => r.Status).HasConversion<string>();
    }
}

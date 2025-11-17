import java.io.IOException;
import java.util.List;
import java.util.Scanner;
// EQUIPE:ESTELA DE SLVA TORRES, RENALDO TENORIO
//KESSIO BARTOLOMEU PAZ FERRO, ALICE ARAUJO DE ALMEIDA
public class AgendaApplication {
    private static AgendaManager manager = new AgendaManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean rodando = true;
        while (rodando) {
            mostrarMenu();
            String opc = scanner.nextLine().trim();
            switch (opc) {
                case "1":
                    opcAdicionar();
                    break;
                case "2":
                    opcBuscar();
                    break;
                case "3":
                    opcRemover();
                    break;
                case "4":
                    opcListarTodos();
                    break;
                case "5":
                    opcSalvar();
                    break;
                case "6":
                    opcCarregar();
                    break;
                case "7":
                    rodando = false;
                    System.out.println("Saindo... tchau!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n- Agenda ");
        System.out.println("1. Adicionar Contato");
        System.out.println("2. Buscar Contato");
        System.out.println("3. Remover Contato");
        System.out.println("4. Listar Todos os Contatos");
        System.out.println("5. Salvar em CSV");
        System.out.println("6. Carregar de CSV");
        System.out.println("7. Sair");
        System.out.print("Escolha: ");
    }

    private static void opcAdicionar() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String tel = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        Contato c = new Contato(nome, tel, email);
        try {
            manager.adicionarContato(c);
            System.out.println("Contato adicionado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void opcBuscar() {
        System.out.print("Nome para buscar: ");
        String nome = scanner.nextLine();
        try {
            Contato c = manager.buscarContato(nome);
            System.out.println("Encontrado: " + c);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void opcRemover() {
        System.out.print("Nome para remover: ");
        String nome = scanner.nextLine();
        try {
            manager.removerContato(nome);
            System.out.println("Removido.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void opcListarTodos() {
        List<Contato> lista = manager.listarContatosOrdenados();
        if (lista.isEmpty()) {
            System.out.println("Nenhum contato.");
        } else {
            System.out.println("Contatos (ordenados):");
            for (Contato c : lista) {
                System.out.println(c);
            }
        }
    }

    private static void opcSalvar() {
        System.out.print("Nome do arquivo para salvar (ex: contatos.csv): ");
        String nomeArquivo = scanner.nextLine();
        try {
            manager.salvarContatosCSV(nomeArquivo);
            System.out.println("Salvo em " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    private static void opcCarregar() {
        System.out.print("Nome do arquivo para carregar (ex: contatos.csv): ");
        String nomeArquivo = scanner.nextLine();
        try {
            manager.carregarContatosCSV(nomeArquivo);
            System.out.println("Carregado de " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
        }
    }
}
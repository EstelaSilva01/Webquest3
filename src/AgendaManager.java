import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaManager implements GerenciadorContatos {
    private List<Contato> contatos = new ArrayList<>();

    @Override
    public void adicionarContato(Contato contato) throws ContatoExistenteException {
        for (Contato c : contatos) {
            if (c.getNome().equalsIgnoreCase(contato.getNome())) {
                throw new ContatoExistenteException("Contato com nome '" + contato.getNome() + "' já existe.");
            }
        }
        contatos.add(contato);
    }

    @Override
    public Contato buscarContato(String nome) throws ContatoNaoEncontradoException {
        for (Contato c : contatos) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        throw new ContatoNaoEncontradoException("Contato com nome '" + nome + "' não encontrado.");
    }

    @Override
    public void removerContato(String nome) throws ContatoNaoEncontradoException {
        Contato encontrado = null;
        for (Contato c : contatos) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                encontrado = c;
                break;
            }
        }
        if (encontrado == null) {
            throw new ContatoNaoEncontradoException("Contato com nome '" + nome + "' não encontrado.");
        }
        contatos.remove(encontrado);
    }

    @Override
    public List<Contato> listarTodosContatos() {
        return new ArrayList<>(contatos);
    }

    // Requisito 5: listar ordenados por nome
    public List<Contato> listarContatosOrdenados() {
        List<Contato> copia = new ArrayList<>(contatos);
        Collections.sort(copia, Comparator.comparing(c -> c.getNome().toLowerCase()));
        return copia;
    }

    // Requisito 5: buscar por domínio do email
    public List<Contato> buscarPorDominioEmail(String dominio) {
        String dominioLower = dominio.toLowerCase();
        return contatos.stream()
                .filter(c -> {
                    String email = c.getEmail();
                    if (email == null) return false;
                    int at = email.indexOf('@');
                    if (at == -1) return false;
                    String dom = email.substring(at + 1).toLowerCase();
                    return dom.equals(dominioLower);
                })
                .collect(Collectors.toList());
    }

    // Requisito 3: salvar CSV (nome;telefone;email)
    public void salvarContatosCSV(String nomeArquivo) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(nomeArquivo))) {
            for (Contato c : contatos) {
                String linha = String.format("%s;%s;%s", escape(c.getNome()), escape(c.getTelefone()), escape(c.getEmail()));
                writer.write(linha);
                writer.newLine();
            }
        }
    }

    // Requisito 3: carregar CSV
    public void carregarContatosCSV(String nomeArquivo) throws IOException {
        List<Contato> carregados = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(nomeArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";", -1);
                String nome = unescape(partes.length > 0 ? partes[0] : "");
                String telefone = unescape(partes.length > 1 ? partes[1] : "");
                String email = unescape(partes.length > 2 ? partes[2] : "");
                carregados.add(new Contato(nome, telefone, email));
            }
        }
        this.contatos = carregados;
    }

    // se precisar escapar ; futuramente (agora simplificado)
    private String escape(String s) {
        return s == null ? "" : s.replace("\n", " ").replace("\r", " ");
    }
    private String unescape(String s) {
        return s == null ? "" : s;
    }
}
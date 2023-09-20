package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Filter{

    private  String contentBuffer;
    public  int[][] filter;
    public  int stride;
    public  int m;
    public  int n;

    public  void Scanner(String filename) throws IOException {
        try {
            contentBuffer = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    public void loadFilter(String filename) throws IOException{
           
        int count = 0;
        Scanner(filename);
        // Separa todos os elementos do arquivo em um vetor de Strings
        String[] headerParts = contentBuffer.split(",");
        // Extrai as informações do cabeçalho
        this.m = Integer.parseInt(headerParts[count].trim()); count++;
        this.n = Integer.parseInt(headerParts[count].trim()); count++;
        this.stride = Integer.parseInt(headerParts[count].trim()); count++;
        
        System.out.println("m: " + m);
        System.out.println("n: " + n);
        System.out.println("stride: " + stride);
        
        if(headerParts.length != (m*n + 3)) {
            System.out.println("Error: O numero de elementos nao corresponde ao tamanho do filtro");
            throw new IOException();
        }
        // Inicia a matriz do filtro
        filter = new int[m][n];
        
        // Preenche a matriz do filtro
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                filter[i][j] = Integer.parseInt(headerParts[count].trim()); count++;
            }
        }

        for (int i = 0; i < m; i++) {
            System.out.println("");
            for (int j = 0; j < n; j++) {
                System.out.print(filter[i][j] + " ");
            }
        }
    }

    public  void main(String[] args) throws IOException {
        // Specify the path to the filter file
        loadFilter("filter.txt");
    }
}

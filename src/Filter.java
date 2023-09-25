package src;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Filter {

  private String contentBuffer;
  public float[][] filter;
  public int stride;
  public int m;
  public int n;

  public void Scanner(String filename) throws IOException {
    try {
      this.contentBuffer = new String(Files.readAllBytes(Paths.get(filename)),
          StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadFilter(String filename) throws IOException {

    int count = 0;
    Scanner(filename);
    // Separa todos os elementos do arquivo em um vetor de Strings
    String[] headerParts = this.contentBuffer.split(",");
    // Extrai as informações do cabeçalho
    this.m = Integer.parseInt(headerParts[count].trim());
    count++;
    this.n = Integer.parseInt(headerParts[count].trim());
    count++;
    this.stride = Integer.parseInt(headerParts[count].trim());
    count++;

    System.out.println("m: " + this.m);
    System.out.println("n: " + this.n);
    System.out.println("stride: " + this.stride);

    if (headerParts.length != (this.m * this.n + 3)) {
      System.out.println("Error: O numero de elementos nao corresponde ao tamanho do filtro");
      throw new IOException();
    }
    // Inicia a matriz do filtro
    this.filter = new float[this.m][this.n];

    // Preenche a matriz do filtro
    for (int i = 0; i < this.m; i++) {
      for (int j = 0; j < this.n; j++) {
        this.filter[i][j] = Float.parseFloat(headerParts[count].trim());
        count++;
      }
    }

    for (int i = 0; i < this.m; i++) {
      System.out.println("");
      for (int j = 0; j < this.n; j++) {
        System.out.print(this.filter[i][j] + " ");
      }
    }
  }

}

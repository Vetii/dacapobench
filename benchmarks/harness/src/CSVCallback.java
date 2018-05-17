/*
 * Copyright (c) 2006, 2009 The Australian National University.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0.
 * You may obtain the license at
 * 
 *    http://www.opensource.org/licenses/apache2.0.php
 * Copyright (c) 2018 Noric Couderc
 */
import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;
import java.util.Date;
import java.io.*;

/**
 * @date $Date: 2009-12-24 11:19:36 +1100 (Thu, 24 Dec 2009) $
 * @id $Id: CSVCallback.java 738 2009-12-24 00:19:36Z steveb-oss $
 * After run of the benchmark, saves results in a CSV file.
 */
public class CSVCallback extends Callback {

  private FileWriter outWriter;

  public CSVCallback(CommandLineArgs args) {
    super(args);
    try {
      outWriter = new FileWriter("out.csv");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  /* Immediately prior to start of the benchmark */
  @Override
  public void start(String benchmark) {
    System.err.println("my hook starting " + (isWarmup() ? "warmup " : "") + benchmark);
    try {
      outWriter.write(csvHeader());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    super.start(benchmark);
  };

  /* Immediately after the end of the benchmark */
  @Override
  public void stop(long duration) {
    super.stop(duration);
    System.err.println("my hook stopped " + (isWarmup() ? "warmup" : ""));
    System.err.flush();
  };

  public String csvHeader() {
    return "benchmark,date,elapsed time" + System.lineSeparator();
  }

  @Override
  public void complete(String benchmark, boolean valid) {
    super.complete(benchmark, valid);
    System.err.println("my hook " + (valid ? "PASSED " : "FAILED ") + (isWarmup() ? "warmup " : "") + benchmark);
    System.err.flush();
    Date now = new Date();
    try {
      outWriter.write(
          benchmark + "," + now.toString() + "," + elapsed
          );
      outWriter.write(System.lineSeparator());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        outWriter.close();
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  };
}

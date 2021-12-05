package com.example.behiveemanagerver2.beehive.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Beehive {
    Long id;
    String symbol;
    MaterialOfBeehive material;
    MarkOfBeehive mark;
    Boolean isQueen;
    String symbolOfBeehive;

    public Beehive(String symbol, MaterialOfBeehive material, MarkOfBeehive mark, Boolean isQueen) {
        this.symbol = symbol;
        this.material = material;
        this.mark = mark;
        this.isQueen = isQueen;
    }
}

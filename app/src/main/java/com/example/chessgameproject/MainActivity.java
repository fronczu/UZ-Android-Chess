package com.example.chessgameproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Boolean FirstPlayerTurn;
    public ArrayList<Coordinates> listOfCoordinates = new ArrayList<>();
    public Position[][] Board = new Position[8][8];
    public Position[][] Board2 = new Position[8][8];
    public Boolean AnythingSelected = false;
    public Coordinates lastPos = null;
    public Coordinates clickedPosition = new Coordinates(0, 0);
    public TextView gameOverView;
    public TextView[][] DisplayBoard = new TextView[8][8];
    public TextView[][] DisplayBoardBackground = new TextView[8][8];
    public ArrayList<Position[][]> LastMoves = new ArrayList<>();
    public LinearLayout pawn_choices;
    public int numberOfMoves;
    TextView textView;

    Piece bKing;
    Piece wKing;

    Piece bQueen;
    Piece wQueen;

    Piece bKnight1;
    Piece bKnight2;
    Piece wKnight1;
    Piece wKnight2;

    Piece bRook1;
    Piece bRook2;
    Piece wRook1;
    Piece wRook2;

    Piece bBishop1;
    Piece bBishop2;
    Piece wBishop1;
    Piece wBishop2;

    Piece bPawn1;
    Piece bPawn2;
    Piece bPawn3;
    Piece bPawn4;
    Piece bPawn5;
    Piece bPawn6;
    Piece bPawn7;
    Piece bPawn8;

    Piece wPawn1;
    Piece wPawn2;
    Piece wPawn3;
    Piece wPawn4;
    Piece wPawn5;
    Piece wPawn6;
    Piece wPawn7;
    Piece wPawn8;


    String vsInfo;
    Button button;
    CountDownTimer timer;
    public int counter=0;
    SQLiteDatabase database;
    protected  void startDatabase(){  //inicjalizacja bazy danych
        database = openOrCreateDatabase("Highscore", MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Users(Username varchar, Time bigint);");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startDatabase();
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){ //zmiana motywu strony w zaleznosci od theme uzytkownika
            setTheme(R.style.Theme_ChessGameProject);
        }else{
            setTheme(R.style.Theme_ChessGameProject);
        }

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);

        Switch Switch=findViewById(R.id.switch1);
        Switch.setChecked(false);
        Bundle b = getIntent().getExtras();
        vsInfo = b.getString("Data"); //odczytywanie nazw graczy z okna menu
        TextView viewVs =(TextView) findViewById(R.id.textView5);
        viewVs.setText(vsInfo);

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            Switch.setChecked(true);
        }
        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //wykrywanie zmiany motywu
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                reset();
            }
        });

        initializeBoard();

        gameOverView = (TextView) findViewById(R.id.game_over);
        pawn_choices = (LinearLayout) findViewById(R.id.pawn_chioces);

        gameOverView.setVisibility(View.INVISIBLE);
        pawn_choices.setVisibility(View.INVISIBLE);
        textView= (TextView) findViewById(R.id.VSinfo);

        timer = new CountDownTimer(150000, 1000){ //odliczanie czasu gry
            public void onTick(long millisUntilFinished){
                textView.setText(String.valueOf(counter));
                counter++;
            }
            public  void onFinish(){
                textView.setText("FINISH!!");
            }
        }.start();
    }
    public void Gameover(){ //wyswietlanie ekranu koncowego i zapisywanie nazwy graczy i czasu do highscore
        gameOverView.setVisibility(View.VISIBLE);
       // Log.i("223","Koniec gry,czas to "+ counter);
        SQLiteStatement smtm = database.compileStatement("INSERT INTO Users VALUES(?,?)");
        smtm.bindString(1,vsInfo);
        smtm.bindLong(2,counter);
        smtm.execute();
        smtm.close();
    }

    private void reset() {
        Intent intent= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setBoard() { //funkcja ukladajaca pionki na starcie gry

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                Piece p = Board[i][j].getPiece();
                int x;

                if (Board[i][j].getPiece() != null) {
                    if (p instanceof King) x = 0;
                    else if (p instanceof Queen) x = 1;
                    else if (p instanceof Rook) x = 2;
                    else if (p instanceof Bishop) x = 3;
                    else if (p instanceof Knight) x = 4;
                    else if (p instanceof Pawn) x = 5;
                    else x = 6;

                    switch (x) {
                        case 0:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wking);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.bking);
                            }
                            break;

                        case 1:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wqueen);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.bqueen);
                            }
                            break;

                        case 2:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wrook);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.brook);
                            }
                            break;

                        case 3:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wbishop);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.bbishop);
                            }
                            break;

                        case 4:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wknight);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.bknight);
                            }
                            break;

                        case 5:
                            if (p.isWhite()) {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.wpawn);
                            } else {
                                DisplayBoard[i][j].setBackgroundResource(R.drawable.bpawn);
                            }
                            break;

                        default:

                    }
                } else {
                    DisplayBoard[i][j].setBackgroundResource(0);
                }
            }
        }
        isKingInDanger();
    }

    @Override
    public void onClick(View v) { //ustalanie koordynatow dotknietego pola

        switch (v.getId()) {
            case R.id.R00:
                clickedPosition = new Coordinates(0, 0);
                break;
            case R.id.R10:
                clickedPosition.setX(1);
                clickedPosition.setY(0);
                break;
            case R.id.R20:
                clickedPosition.setX(2);
                clickedPosition.setY(0);
                break;
            case R.id.R30:
                clickedPosition.setX(3);
                clickedPosition.setY(0);
                break;
            case R.id.R40:
                clickedPosition.setX(4);
                clickedPosition.setY(0);
                break;
            case R.id.R50:
                clickedPosition.setX(5);
                clickedPosition.setY(0);
                break;
            case R.id.R60:
                clickedPosition.setX(6);
                clickedPosition.setY(0);
                break;
            case R.id.R70:
                clickedPosition.setX(7);
                clickedPosition.setY(0);
                break;

            case R.id.R01:
                clickedPosition.setX(0);
                clickedPosition.setY(1);
                break;
            case R.id.R11:
                clickedPosition.setX(1);
                clickedPosition.setY(1);
                break;
            case R.id.R21:
                clickedPosition.setX(2);
                clickedPosition.setY(1);
                break;
            case R.id.R31:
                clickedPosition.setX(3);
                clickedPosition.setY(1);
                break;
            case R.id.R41:
                clickedPosition.setX(4);
                clickedPosition.setY(1);
                break;
            case R.id.R51:
                clickedPosition.setX(5);
                clickedPosition.setY(1);
                break;
            case R.id.R61:
                clickedPosition.setX(6);
                clickedPosition.setY(1);
                break;
            case R.id.R71:
                clickedPosition.setX(7);
                clickedPosition.setY(1);
                break;

            case R.id.R02:
                clickedPosition.setX(0);
                clickedPosition.setY(2);
                break;
            case R.id.R12:
                clickedPosition.setX(1);
                clickedPosition.setY(2);
                break;
            case R.id.R22:
                clickedPosition.setX(2);
                clickedPosition.setY(2);
                break;
            case R.id.R32:
                clickedPosition.setX(3);
                clickedPosition.setY(2);
                break;
            case R.id.R42:
                clickedPosition.setX(4);
                clickedPosition.setY(2);
                break;
            case R.id.R52:
                clickedPosition.setX(5);
                clickedPosition.setY(2);
                break;
            case R.id.R62:
                clickedPosition.setX(6);
                clickedPosition.setY(2);
                break;
            case R.id.R72:
                clickedPosition.setX(7);
                clickedPosition.setY(2);
                break;

            case R.id.R03:
                clickedPosition.setX(0);
                clickedPosition.setY(3);
                break;
            case R.id.R13:
                clickedPosition.setX(1);
                clickedPosition.setY(3);
                break;
            case R.id.R23:
                clickedPosition.setX(2);
                clickedPosition.setY(3);
                break;
            case R.id.R33:
                clickedPosition.setX(3);
                clickedPosition.setY(3);
                break;
            case R.id.R43:
                clickedPosition.setX(4);
                clickedPosition.setY(3);
                break;
            case R.id.R53:
                clickedPosition.setX(5);
                clickedPosition.setY(3);
                break;
            case R.id.R63:
                clickedPosition.setX(6);
                clickedPosition.setY(3);
                break;
            case R.id.R73:
                clickedPosition.setX(7);
                clickedPosition.setY(3);
                break;

            case R.id.R04:
                clickedPosition.setX(0);
                clickedPosition.setY(4);
                break;
            case R.id.R14:
                clickedPosition.setX(1);
                clickedPosition.setY(4);
                break;
            case R.id.R24:
                clickedPosition.setX(2);
                clickedPosition.setY(4);
                break;
            case R.id.R34:
                clickedPosition.setX(3);
                clickedPosition.setY(4);
                break;
            case R.id.R44:
                clickedPosition.setX(4);
                clickedPosition.setY(4);
                break;
            case R.id.R54:
                clickedPosition.setX(5);
                clickedPosition.setY(4);
                break;
            case R.id.R64:
                clickedPosition.setX(6);
                clickedPosition.setY(4);
                break;
            case R.id.R74:
                clickedPosition.setX(7);
                clickedPosition.setY(4);
                break;

            case R.id.R05:
                clickedPosition.setX(0);
                clickedPosition.setY(5);
                break;
            case R.id.R15:
                clickedPosition.setX(1);
                clickedPosition.setY(5);
                break;
            case R.id.R25:
                clickedPosition.setX(2);
                clickedPosition.setY(5);
                break;
            case R.id.R35:
                clickedPosition.setX(3);
                clickedPosition.setY(5);
                break;
            case R.id.R45:
                clickedPosition.setX(4);
                clickedPosition.setY(5);
                break;
            case R.id.R55:
                clickedPosition.setX(5);
                clickedPosition.setY(5);
                break;
            case R.id.R65:
                clickedPosition.setX(6);
                clickedPosition.setY(5);
                break;
            case R.id.R75:
                clickedPosition.setX(7);
                clickedPosition.setY(5);
                break;

            case R.id.R06:
                clickedPosition.setX(0);
                clickedPosition.setY(6);
                break;
            case R.id.R16:
                clickedPosition.setX(1);
                clickedPosition.setY(6);
                break;
            case R.id.R26:
                clickedPosition.setX(2);
                clickedPosition.setY(6);
                break;
            case R.id.R36:
                clickedPosition.setX(3);
                clickedPosition.setY(6);
                break;
            case R.id.R46:
                clickedPosition.setX(4);
                clickedPosition.setY(6);
                break;
            case R.id.R56:
                clickedPosition.setX(5);
                clickedPosition.setY(6);
                break;
            case R.id.R66:
                clickedPosition.setX(6);
                clickedPosition.setY(6);
                break;
            case R.id.R76:
                clickedPosition.setX(7);
                clickedPosition.setY(6);
                break;

            case R.id.R07:
                clickedPosition.setX(0);
                clickedPosition.setY(7);
                break;
            case R.id.R17:
                clickedPosition.setX(1);
                clickedPosition.setY(7);
                break;
            case R.id.R27:
                clickedPosition.setX(2);
                clickedPosition.setY(7);
                break;
            case R.id.R37:
                clickedPosition.setX(3);
                clickedPosition.setY(7);
                break;
            case R.id.R47:
                clickedPosition.setX(4);
                clickedPosition.setY(7);
                break;
            case R.id.R57:
                clickedPosition.setX(5);
                clickedPosition.setY(7);
                break;
            case R.id.R67:
                clickedPosition.setX(6);
                clickedPosition.setY(7);
                break;
            case R.id.R77:
                clickedPosition.setX(7);
                clickedPosition.setY(7);
                break;
        }

        if (!AnythingSelected) { //sprawdzanie aktualnych mozliwych pol dla wybranego pionka na danym polu
            if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() == null) {
                isKingInDanger();
                return;
            } else {
                if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {
                    isKingInDanger();
                    return;
                } else {
                    listOfCoordinates.clear();
                    listOfCoordinates = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().AllowedMoves(clickedPosition, Board);
                    DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
                    setColorAtAllowedPosition(listOfCoordinates);
                    AnythingSelected = true;
                }
            }
        } else {
            if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() == null) {
                if (moveIsAllowed(listOfCoordinates, clickedPosition)) {

                    saveBoard();
                    if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof King) { // sprawdzamy czy bijemy krola
                        if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {    //koniec gry
                            Gameover();
                        }
                    }
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(Board[lastPos.getX()][lastPos.getY()].getPiece());
                    Board[lastPos.getX()][lastPos.getY()].setPiece(null);

                    isKingInDanger();
                    resetColorAtAllowedPosition(listOfCoordinates);
                    DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                    resetColorAtLastPosition(lastPos);
                    AnythingSelected = false;
                    FirstPlayerTurn = !FirstPlayerTurn;
                    checkForPawn();

                } else {
                    resetColorAtLastPosition(lastPos);
                    resetColorAtAllowedPosition(listOfCoordinates);
                    AnythingSelected = false;
                }

            } else {
                if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() == null) {
                    isKingInDanger();
                    return;

                } else {
                    if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() != null) {
                        if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {
                            if (moveIsAllowed(listOfCoordinates, clickedPosition)) {

                                saveBoard();
                                if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof King) {// sprawdzamy czy bijemy krola
                                    if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) { //koniec gry
                                        Gameover();
                                    }
                                }
                                Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(Board[lastPos.getX()][lastPos.getY()].getPiece());
                                Board[lastPos.getX()][lastPos.getY()].setPiece(null);

                                resetColorAtAllowedPosition(listOfCoordinates);
                                DisplayBoard[lastPos.getX()][lastPos.getY()].setBackgroundResource(0);
                                resetColorAtLastPosition(lastPos);

                                AnythingSelected = false;
                                FirstPlayerTurn = !FirstPlayerTurn;
                                checkForPawn();
                            } else {
                                resetColorAtLastPosition(lastPos);
                                resetColorAtAllowedPosition(listOfCoordinates);
                                AnythingSelected = false;
                            }

                        } else {
                            if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite() != FirstPlayerTurn) {
                                isKingInDanger();
                                return;
                            }

                            resetColorAtLastPosition(lastPos);
                            resetColorAtAllowedPosition(listOfCoordinates);

                            listOfCoordinates.clear();
                            listOfCoordinates = Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().AllowedMoves(clickedPosition, Board);
                            DisplayBoardBackground[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.color.colorSelected);
                            setColorAtAllowedPosition(listOfCoordinates);
                            AnythingSelected = true;
                        }
                    }
                }
            }
        }

        isKingInDanger();
        lastPos = new Coordinates(clickedPosition.getX(), clickedPosition.getY());
        setBoard();
    }

    public void saveBoard() { //zapisywanie poprzedniego ruchu
        numberOfMoves++;
        LastMoves.add(numberOfMoves - 1, Board2);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                LastMoves.get(numberOfMoves - 1)[i][j] = new Position(null);
            }
        }

        for (int g = 0; g < 8; g++) {
            for (int h = 0; h < 8; h++) {
                if (Board[g][h].getPiece() == null) {
                    LastMoves.get(numberOfMoves - 1)[g][h].setPiece(null);
                } else {
                    LastMoves.get(numberOfMoves - 1)[g][h].setPiece(Board[g][h].getPiece());
                }
            }
        }
    }

    public void pawnChoice(View v) { //wybor jaki obrazek ma zostac wrzucony w jakie pole
        int x = v.getId();
        switch (x) {
            case R.id.pawn_queen:
                if (clickedPosition.getY() == 0) {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Queen(true));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.wqueen);
                } else {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Queen(false));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.bqueen);
                }
                break;
            case R.id.pawn_rook:
                if (clickedPosition.getY() == 0) {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Rook(true));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.wrook);
                } else {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Rook(false));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.brook);
                }
                break;
            case R.id.pawn_bishop:
                if (clickedPosition.getY() == 0) {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Bishop(true));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.wbishop);
                } else {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Bishop(false));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.bbishop);
                }
                break;
            case R.id.pawn_knight:
                if (clickedPosition.getY() == 0) {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Knight(true));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.wknight);
                } else {
                    Board[clickedPosition.getX()][clickedPosition.getY()].setPiece(new Knight(false));
                    DisplayBoard[clickedPosition.getX()][clickedPosition.getY()].setBackgroundResource(R.drawable.bknight);

                }
                break;
        }
        pawn_choices.setVisibility(View.INVISIBLE);
    }

    private void resetColorAtAllowedPosition(ArrayList<Coordinates> listOfCoordinates) { //po ruchu pionka przywrocenie odpowiedniego pola szachownicy
        for (int i = 0; i < listOfCoordinates.size(); i++) {
            if ((listOfCoordinates.get(i).getX() + listOfCoordinates.get(i).getY()) % 2 == 0) {
                DisplayBoardBackground[listOfCoordinates.get(i).getX()][listOfCoordinates.get(i).getY()].setBackgroundResource(R.color.colorBoardDark);
            } else {
                DisplayBoardBackground[listOfCoordinates.get(i).getX()][listOfCoordinates.get(i).getY()].setBackgroundResource(R.color.colorBoardLight);
            }
        }
    }

    void setColorAtAllowedPosition(ArrayList<Coordinates> list) { //podswietlanie pol w zalezonosci czy sa zagrozone

        for (int i = 0; i < list.size(); i++) {
            if (Board[list.get(i).getX()][list.get(i).getY()].getPiece() == null) {
                DisplayBoardBackground[list.get(i).getX()][list.get(i).getY()].setBackgroundResource(R.color.colorPositionAvailable);
            } else {
                DisplayBoardBackground[list.get(i).getX()][list.get(i).getY()].setBackgroundResource(R.color.colorDanger);
            }
        }
    }

    private boolean moveIsAllowed(ArrayList<Coordinates> piece, Coordinates coordinate) { //sprawdzenie czy ruch jest dozwolony
        Boolean Allowed = false;
        for (int i = 0; i < piece.size(); i++) {
            if (piece.get(i).getX() == coordinate.getX() && piece.get(i).getY() == coordinate.getY()) {
                Allowed = true;
                break;
            }
        }
        return Allowed;
    }

    private void resetColorAtLastPosition(Coordinates lastPos) { // resetowanie koloru na ostatniej pozycji
        if ((lastPos.getX() + lastPos.getY()) % 2 == 0) {
            DisplayBoardBackground[lastPos.getX()][lastPos.getY()].setBackgroundResource(R.color.colorBoardDark);
        } else {
            DisplayBoardBackground[lastPos.getX()][lastPos.getY()].setBackgroundResource(R.color.colorBoardLight);
        }
    }

    private void isKingInDanger() {  //sprawdzenie czy krol jest zagrozony
        ArrayList<Coordinates> List = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board[i][j].getPiece() != null) {
                    List.clear();
                    Coordinates c = new Coordinates(i, j);
                    List = Board[i][j].getPiece().AllowedMoves(c, Board);

                    for (int x = 0; x < List.size(); x++) {
                        if (Board[List.get(x).getX()][List.get(x).getY()].getPiece() instanceof King) {
                            if ((List.get(x).getX() + List.get(x).getY()) % 2 == 0) {
                                DisplayBoardBackground[List.get(x).getX()][List.get(x).getY()].setBackgroundResource(R.color.colorBoardDark);
                            } else {
                                DisplayBoardBackground[List.get(x).getX()][List.get(x).getY()].setBackgroundResource(R.color.colorBoardLight);
                            }
                            if (Board[i][j].getPiece().isWhite() != Board[List.get(x).getX()][List.get(x).getY()].getPiece().isWhite()) {
                                DisplayBoardBackground[List.get(x).getX()][List.get(x).getY()].setBackgroundResource(R.color.colorKingInDanger);
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkForPawn() { //jezeli pionek dojdzie do konca planszy, pojawi sie okno awansu dla pionka
        if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece() instanceof Pawn) {
            if (Board[clickedPosition.getX()][clickedPosition.getY()].getPiece().isWhite()) {
                if (clickedPosition.getY() == 0) {
                    pawn_choices.setVisibility(View.VISIBLE);
                }
            } else {
                if (clickedPosition.getY() == 7) {
                    pawn_choices.setVisibility(View.VISIBLE);
                    pawn_choices.setRotation(180);
                }
            }
        }
        isKingInDanger();
    }

    private void initializeBoard() { // inicjalizacja planszy
        bKing = new King(false);
        wKing = new King(true);

        bQueen = new Queen(false);
        wQueen = new Queen(true);

        bRook1 = new Rook(false);
        bRook2 = new Rook(false);
        wRook1 = new Rook(true);
        wRook2 = new Rook(true);

        bKnight1 = new Knight(false);
        bKnight2 = new Knight(false);
        wKnight1 = new Knight(true);
        wKnight2 = new Knight(true);

        bBishop1 = new Bishop(false);
        bBishop2 = new Bishop(false);
        wBishop1 = new Bishop(true);
        wBishop2 = new Bishop(true);

        bPawn1 = new Pawn(false);
        bPawn2 = new Pawn(false);
        bPawn3 = new Pawn(false);
        bPawn4 = new Pawn(false);
        bPawn5 = new Pawn(false);
        bPawn6 = new Pawn(false);
        bPawn7 = new Pawn(false);
        bPawn8 = new Pawn(false);

        wPawn1 = new Pawn(true);
        wPawn2 = new Pawn(true);
        wPawn3 = new Pawn(true);
        wPawn4 = new Pawn(true);
        wPawn5 = new Pawn(true);
        wPawn6 = new Pawn(true);
        wPawn7 = new Pawn(true);
        wPawn8 = new Pawn(true);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board[i][j] = new Position(null);
                Board2[i][j] = new Position(null);
            }
        }

        Board[0][7].setPiece(wRook1);
        Board[1][7].setPiece(wKnight1);
        Board[2][7].setPiece(wBishop1);
        Board[3][7].setPiece(wQueen);
        Board[4][7].setPiece(wKing);
        Board[5][7].setPiece(wBishop2);
        Board[6][7].setPiece(wKnight2);
        Board[7][7].setPiece(wRook2);

        Board[0][6].setPiece(wPawn1);
        Board[1][6].setPiece(wPawn2);
        Board[2][6].setPiece(wPawn3);
        Board[3][6].setPiece(wPawn4);
        Board[4][6].setPiece(wPawn5);
        Board[5][6].setPiece(wPawn6);
        Board[6][6].setPiece(wPawn7);
        Board[7][6].setPiece(wPawn8);

        Board[0][0].setPiece(bRook1);
        Board[1][0].setPiece(bKnight1);
        Board[2][0].setPiece(bBishop1);
        Board[3][0].setPiece(bQueen);
        Board[4][0].setPiece(bKing);
        Board[5][0].setPiece(bBishop2);
        Board[6][0].setPiece(bKnight2);
        Board[7][0].setPiece(bRook2);

        Board[0][1].setPiece(bPawn1);
        Board[1][1].setPiece(bPawn2);
        Board[2][1].setPiece(bPawn3);
        Board[3][1].setPiece(bPawn4);
        Board[4][1].setPiece(bPawn5);
        Board[5][1].setPiece(bPawn6);
        Board[6][1].setPiece(bPawn7);
        Board[7][1].setPiece(bPawn8);

        DisplayBoard[0][0] = (TextView) findViewById(R.id.R00);
        DisplayBoardBackground[0][0] = (TextView) findViewById(R.id.R000);
        DisplayBoard[1][0] = (TextView) findViewById(R.id.R10);
        DisplayBoardBackground[1][0] = (TextView) findViewById(R.id.R010);
        DisplayBoard[2][0] = (TextView) findViewById(R.id.R20);
        DisplayBoardBackground[2][0] = (TextView) findViewById(R.id.R020);
        DisplayBoard[3][0] = (TextView) findViewById(R.id.R30);
        DisplayBoardBackground[3][0] = (TextView) findViewById(R.id.R030);
        DisplayBoard[4][0] = (TextView) findViewById(R.id.R40);
        DisplayBoardBackground[4][0] = (TextView) findViewById(R.id.R040);
        DisplayBoard[5][0] = (TextView) findViewById(R.id.R50);
        DisplayBoardBackground[5][0] = (TextView) findViewById(R.id.R050);
        DisplayBoard[6][0] = (TextView) findViewById(R.id.R60);
        DisplayBoardBackground[6][0] = (TextView) findViewById(R.id.R060);
        DisplayBoard[7][0] = (TextView) findViewById(R.id.R70);
        DisplayBoardBackground[7][0] = (TextView) findViewById(R.id.R070);

        DisplayBoard[0][1] = (TextView) findViewById(R.id.R01);
        DisplayBoardBackground[0][1] = (TextView) findViewById(R.id.R001);
        DisplayBoard[1][1] = (TextView) findViewById(R.id.R11);
        DisplayBoardBackground[1][1] = (TextView) findViewById(R.id.R011);
        DisplayBoard[2][1] = (TextView) findViewById(R.id.R21);
        DisplayBoardBackground[2][1] = (TextView) findViewById(R.id.R021);
        DisplayBoard[3][1] = (TextView) findViewById(R.id.R31);
        DisplayBoardBackground[3][1] = (TextView) findViewById(R.id.R031);
        DisplayBoard[4][1] = (TextView) findViewById(R.id.R41);
        DisplayBoardBackground[4][1] = (TextView) findViewById(R.id.R041);
        DisplayBoard[5][1] = (TextView) findViewById(R.id.R51);
        DisplayBoardBackground[5][1] = (TextView) findViewById(R.id.R051);
        DisplayBoard[6][1] = (TextView) findViewById(R.id.R61);
        DisplayBoardBackground[6][1] = (TextView) findViewById(R.id.R061);
        DisplayBoard[7][1] = (TextView) findViewById(R.id.R71);
        DisplayBoardBackground[7][1] = (TextView) findViewById(R.id.R071);

        DisplayBoard[0][2] = (TextView) findViewById(R.id.R02);
        DisplayBoardBackground[0][2] = (TextView) findViewById(R.id.R002);
        DisplayBoard[1][2] = (TextView) findViewById(R.id.R12);
        DisplayBoardBackground[1][2] = (TextView) findViewById(R.id.R012);
        DisplayBoard[2][2] = (TextView) findViewById(R.id.R22);
        DisplayBoardBackground[2][2] = (TextView) findViewById(R.id.R022);
        DisplayBoard[3][2] = (TextView) findViewById(R.id.R32);
        DisplayBoardBackground[3][2] = (TextView) findViewById(R.id.R032);
        DisplayBoard[4][2] = (TextView) findViewById(R.id.R42);
        DisplayBoardBackground[4][2] = (TextView) findViewById(R.id.R042);
        DisplayBoard[5][2] = (TextView) findViewById(R.id.R52);
        DisplayBoardBackground[5][2] = (TextView) findViewById(R.id.R052);
        DisplayBoard[6][2] = (TextView) findViewById(R.id.R62);
        DisplayBoardBackground[6][2] = (TextView) findViewById(R.id.R062);
        DisplayBoard[7][2] = (TextView) findViewById(R.id.R72);
        DisplayBoardBackground[7][2] = (TextView) findViewById(R.id.R072);

        DisplayBoard[0][3] = (TextView) findViewById(R.id.R03);
        DisplayBoardBackground[0][3] = (TextView) findViewById(R.id.R003);
        DisplayBoard[1][3] = (TextView) findViewById(R.id.R13);
        DisplayBoardBackground[1][3] = (TextView) findViewById(R.id.R013);
        DisplayBoard[2][3] = (TextView) findViewById(R.id.R23);
        DisplayBoardBackground[2][3] = (TextView) findViewById(R.id.R023);
        DisplayBoard[3][3] = (TextView) findViewById(R.id.R33);
        DisplayBoardBackground[3][3] = (TextView) findViewById(R.id.R033);
        DisplayBoard[4][3] = (TextView) findViewById(R.id.R43);
        DisplayBoardBackground[4][3] = (TextView) findViewById(R.id.R043);
        DisplayBoard[5][3] = (TextView) findViewById(R.id.R53);
        DisplayBoardBackground[5][3] = (TextView) findViewById(R.id.R053);
        DisplayBoard[6][3] = (TextView) findViewById(R.id.R63);
        DisplayBoardBackground[6][3] = (TextView) findViewById(R.id.R063);
        DisplayBoard[7][3] = (TextView) findViewById(R.id.R73);
        DisplayBoardBackground[7][3] = (TextView) findViewById(R.id.R073);

        DisplayBoard[0][4] = (TextView) findViewById(R.id.R04);
        DisplayBoardBackground[0][4] = (TextView) findViewById(R.id.R004);
        DisplayBoard[1][4] = (TextView) findViewById(R.id.R14);
        DisplayBoardBackground[1][4] = (TextView) findViewById(R.id.R014);
        DisplayBoard[2][4] = (TextView) findViewById(R.id.R24);
        DisplayBoardBackground[2][4] = (TextView) findViewById(R.id.R024);
        DisplayBoard[3][4] = (TextView) findViewById(R.id.R34);
        DisplayBoardBackground[3][4] = (TextView) findViewById(R.id.R034);
        DisplayBoard[4][4] = (TextView) findViewById(R.id.R44);
        DisplayBoardBackground[4][4] = (TextView) findViewById(R.id.R044);
        DisplayBoard[5][4] = (TextView) findViewById(R.id.R54);
        DisplayBoardBackground[5][4] = (TextView) findViewById(R.id.R054);
        DisplayBoard[6][4] = (TextView) findViewById(R.id.R64);
        DisplayBoardBackground[6][4] = (TextView) findViewById(R.id.R064);
        DisplayBoard[7][4] = (TextView) findViewById(R.id.R74);
        DisplayBoardBackground[7][4] = (TextView) findViewById(R.id.R074);

        DisplayBoard[0][5] = (TextView) findViewById(R.id.R05);
        DisplayBoardBackground[0][5] = (TextView) findViewById(R.id.R005);
        DisplayBoard[1][5] = (TextView) findViewById(R.id.R15);
        DisplayBoardBackground[1][5] = (TextView) findViewById(R.id.R015);
        DisplayBoard[2][5] = (TextView) findViewById(R.id.R25);
        DisplayBoardBackground[2][5] = (TextView) findViewById(R.id.R025);
        DisplayBoard[3][5] = (TextView) findViewById(R.id.R35);
        DisplayBoardBackground[3][5] = (TextView) findViewById(R.id.R035);
        DisplayBoard[4][5] = (TextView) findViewById(R.id.R45);
        DisplayBoardBackground[4][5] = (TextView) findViewById(R.id.R045);
        DisplayBoard[5][5] = (TextView) findViewById(R.id.R55);
        DisplayBoardBackground[5][5] = (TextView) findViewById(R.id.R055);
        DisplayBoard[6][5] = (TextView) findViewById(R.id.R65);
        DisplayBoardBackground[6][5] = (TextView) findViewById(R.id.R065);
        DisplayBoard[7][5] = (TextView) findViewById(R.id.R75);
        DisplayBoardBackground[7][5] = (TextView) findViewById(R.id.R075);

        DisplayBoard[0][6] = (TextView) findViewById(R.id.R06);
        DisplayBoardBackground[0][6] = (TextView) findViewById(R.id.R006);
        DisplayBoard[1][6] = (TextView) findViewById(R.id.R16);
        DisplayBoardBackground[1][6] = (TextView) findViewById(R.id.R016);
        DisplayBoard[2][6] = (TextView) findViewById(R.id.R26);
        DisplayBoardBackground[2][6] = (TextView) findViewById(R.id.R026);
        DisplayBoard[3][6] = (TextView) findViewById(R.id.R36);
        DisplayBoardBackground[3][6] = (TextView) findViewById(R.id.R036);
        DisplayBoard[4][6] = (TextView) findViewById(R.id.R46);
        DisplayBoardBackground[4][6] = (TextView) findViewById(R.id.R046);
        DisplayBoard[5][6] = (TextView) findViewById(R.id.R56);
        DisplayBoardBackground[5][6] = (TextView) findViewById(R.id.R056);
        DisplayBoard[6][6] = (TextView) findViewById(R.id.R66);
        DisplayBoardBackground[6][6] = (TextView) findViewById(R.id.R066);
        DisplayBoard[7][6] = (TextView) findViewById(R.id.R76);
        DisplayBoardBackground[7][6] = (TextView) findViewById(R.id.R076);

        DisplayBoard[0][7] = (TextView) findViewById(R.id.R07);
        DisplayBoardBackground[0][7] = (TextView) findViewById(R.id.R007);
        DisplayBoard[1][7] = (TextView) findViewById(R.id.R17);
        DisplayBoardBackground[1][7] = (TextView) findViewById(R.id.R017);
        DisplayBoard[2][7] = (TextView) findViewById(R.id.R27);
        DisplayBoardBackground[2][7] = (TextView) findViewById(R.id.R027);
        DisplayBoard[3][7] = (TextView) findViewById(R.id.R37);
        DisplayBoardBackground[3][7] = (TextView) findViewById(R.id.R037);
        DisplayBoard[4][7] = (TextView) findViewById(R.id.R47);
        DisplayBoardBackground[4][7] = (TextView) findViewById(R.id.R047);
        DisplayBoard[5][7] = (TextView) findViewById(R.id.R57);
        DisplayBoardBackground[5][7] = (TextView) findViewById(R.id.R057);
        DisplayBoard[6][7] = (TextView) findViewById(R.id.R67);
        DisplayBoardBackground[6][7] = (TextView) findViewById(R.id.R067);
        DisplayBoard[7][7] = (TextView) findViewById(R.id.R77);
        DisplayBoardBackground[7][7] = (TextView) findViewById(R.id.R077);

        for (int g = 0; g < 8; g++) {
            for (int h = 0; h < 8; h++) {
                if (Board[g][h].getPiece() == null) {
                    Board2[g][h].setPiece(null);
                } else {
                    Board2[g][h].setPiece(Board[g][h].getPiece());
                }
            }
        }

        numberOfMoves = 0;
        AnythingSelected = false;
        FirstPlayerTurn = true;
        setBoard();
    }
}
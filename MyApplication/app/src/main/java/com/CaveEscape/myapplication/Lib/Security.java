package com.CaveEscape.myapplication.Lib;

public class Security
{
    /*
        Create a random key for idClient
     */
    public static String RandomToken(Integer nbLetters)
    {
        StringBuilder randString = new StringBuilder();
        String charUniverse = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\\=";

        for( int i = 0; i < nbLetters; i++)
        {
            randString.append( charUniverse.charAt( (int) (Math.random() * ( charUniverse.length() - 1))) );
        }
        return randString.toString();
    }
}

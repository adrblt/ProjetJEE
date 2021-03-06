package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entities.Utilisateur;
import static dao.DAOUtilitaires.*;

public class UtilisateurDaoImpl{
	private static final String SQL_SELECT_PAR_EMAIL = "SELECT idUtilisateur, email, login, mdp, dateInscription, vRole FROM bdd_jee.Utilisateur u, bdd_jee.Role r WHERE email = ? and u.idRole = r.idRole";
	private static final String SQL_INSERT = "INSERT INTO bdd_jee.Utilisateur (email, mdp, login, dateInscription) VALUES (?, ?, ?, NOW())";
	
	public Utilisateur trouver( String email ) throws DAOException {
	    Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    Utilisateur utilisateur = null;
	    try {
	        connexion = getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_PAR_EMAIL, false, email );
	        resultSet = preparedStatement.executeQuery();
	        if ( resultSet.next() ) {
	            utilisateur = map( resultSet );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( resultSet, preparedStatement, connexion );
	    }

	    return utilisateur;
	}

	public void creer( Utilisateur utilisateur ) throws DAOException {
	    Connection connexion = null;
	    PreparedStatement preparedStatement = null;
	    ResultSet valeursAutoGenerees = null;

	    try {
	        /* Récupération d'une connexion depuis la Factory */
	        connexion = getConnection();
	        preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true, utilisateur.getEmail(), utilisateur.getMotDePasse(), utilisateur.getNom() );
	        int statut = preparedStatement.executeUpdate();
	        /* Analyse du statut retourné par la requête d'insertion */
	        if ( statut == 0 ) {
	            throw new DAOException( "Échec de la création de l'utilisateur, aucune ligne ajoutée dans la table." );
	        }
	        /* Récupération de l'id auto-généré par la requête d'insertion */
	        valeursAutoGenerees = preparedStatement.getGeneratedKeys();
	        if ( valeursAutoGenerees.next() ) {
	            /* Puis initialisation de la propriété id du bean Utilisateur avec sa valeur */
	            utilisateur.setId( valeursAutoGenerees.getLong( 1 ) );
	        } else {
	            throw new DAOException( "Échec de la création de l'utilisateur en base, aucun ID auto-généré retourné." );
	        }
	    } catch ( SQLException e ) {
	        throw new DAOException( e );
	    } finally {
	        fermeturesSilencieuses( valeursAutoGenerees, preparedStatement, connexion );
	    }
	}
    
	private static Utilisateur map( ResultSet resultSet ) throws SQLException {
	    Utilisateur utilisateur = new Utilisateur();
	    utilisateur.setId( resultSet.getLong( "idUtilisateur" ) );
	    utilisateur.setEmail( resultSet.getString( "email" ) );
	    utilisateur.setMotDePasse( resultSet.getString( "mdp" ) );
	    utilisateur.setNom( resultSet.getString( "login" ) );
	    utilisateur.setDateInscription( resultSet.getTimestamp( "dateInscription" ) );
	    utilisateur.setRole( resultSet.getString( "vRole" ) );
	    return utilisateur;
	}
}

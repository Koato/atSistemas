package com.at.sistema.prueba;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;

/**
 * Mejorar cada uno de los métodos a nivel SQL y código cuando sea necesario
 * Razonar cada una de las mejoras que se han implementado No es necesario que
 * el código implementado funcione
 */
public class TestSqlDao {

	private Hashtable<Long, Long> maxOrderUser;

	private Connection getConnection() {
		// return JDBC connection
		return null;
	}

	/**
	 * Obtiene el ID del último pedido para cada usuario
	 */
	public Hashtable<Long, Long> getMaxUserOrderId(long idTienda) throws Exception {
		StringBuilder query = new StringBuilder("SELECT ID_PEDIDO, ID_USUARIO ");
		query.append("FROM PEDIDOS ");
		query.append("WHERE ID_TIENDA = ? ");
		query.append("ORDER BY ID_PEDIDO DESC");

		maxOrderUser = new Hashtable<Long, Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Connection connection = getConnection();
			stmt = connection.prepareStatement(query.toString());
			stmt.setLong(1, idTienda);
			rs = stmt.executeQuery();
			while (rs.next()) {
				long idPedido = rs.getInt("ID_PEDIDO");
				long idUsuario = rs.getInt("ID_USUARIO");
				if (!maxOrderUser.containsKey(idUsuario)) {
					maxOrderUser.put(idUsuario, idPedido);
				} else if (maxOrderUser.get(idUsuario) < idPedido) {
					maxOrderUser.put(idUsuario, idPedido);
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			rs = null;
			if (stmt != null) {
				stmt.close();
			}
			stmt = null;
		}
		return maxOrderUser;
	}

	/**
	 * Copia todos los pedidos de un usuario a otro
	 */
	public void copyUserOrders(long idUserOri, long idUserDes) throws Exception {
		StringBuilder query = new StringBuilder("SELECT FECHA, TOTAL, SUBTOTAL, DIRECCION ");
		query.append("FROM PEDIDOS ");
		query.append("WHERE ID_USUARIO = ? ");

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			Connection connection = getConnection();
			stmt = connection.prepareStatement(query.toString());
			stmt.setLong(1, idUserOri);
			rs = stmt.executeQuery();
			while (rs.next()) {
				StringBuilder insert = new StringBuilder("INSERT INTO PEDIDOS ");
				insert.append("(FECHA, TOTAL, SUBTOTAL, DIRECCION) VALUES ");
				insert.append("(?, ?, ?, ?)");
				PreparedStatement stmt2 = null;
				Connection connection2 = getConnection();
				try {
					connection2.setAutoCommit(false);
					stmt2 = connection2.prepareStatement(insert.toString());
					stmt2.setTimestamp(1, rs.getTimestamp("FECHA"));
					stmt2.setDouble(2, rs.getDouble("TOTAL"));
					stmt2.setDouble(3, rs.getDouble("SUBTOTAL"));
					stmt2.setString(4, rs.getString("DIRECCION"));
					stmt2.executeUpdate();
					connection2.commit();
				} catch (Exception e) {
					connection2.rollback();
				} finally {
					if (stmt2 != null) {
						stmt2.close();
					}
					stmt2 = null;
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			rs = null;
			if (stmt != null) {
				stmt.close();
			}
			stmt = null;
		}
	}

	/**
	 * Obtiene los datos del usuario y pedido con el pedido de mayor importe para la
	 * tienda dada
	 */
	public void getUserMaxOrder(long idTienda, long userId, long orderId, String name, String address)
			throws Exception {
		StringBuilder query = new StringBuilder("SELECT U.ID_USUARIO, P.ID_PEDIDO, P.TOTAL, U.NOMBRE, U.DIRECCION ");
		query.append("FROM PEDIDOS AS P ");
		query.append("INNER JOIN USUARIOS AS U ");
		query.append("ON P.ID_USUARIO = U.ID_USUARIO ");
		query.append("WHERE P.ID_TIENDA = ?");
		query.append("ORDER BY P.TOTAL DESC");

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			Connection connection = getConnection();
			stmt = connection.prepareStatement(query.toString());
			stmt.setLong(1, idTienda);
			rs = stmt.executeQuery();
			double total = 0;
			while (rs.next()) {
				if (rs.getLong("TOTAL") > total) {
					total = rs.getLong("TOTAL");
					userId = rs.getInt("ID_USUARIO");
					orderId = rs.getInt("ID_PEDIDO");
					name = rs.getString("NOMBRE");
					address = rs.getString("DIRECCION");
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			rs = null;
			if (stmt != null) {
				stmt.close();
			}
			stmt = null;
		}
	}
}

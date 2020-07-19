package com.brittus.application;

import java.math.BigDecimal;
import java.util.List;

import javax.money.Monetary;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.brittus.infra.FamiliaDB;
import com.brittus.infra.FundoFamiliarDB;
import com.brittus.infra.FamiliaDB.MembroDaFamiliaDB;

import org.bson.types.ObjectId;

@Path("/familias")
public class FamiliaResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FamiliaDB> getTodasAsFamilias() {
        return FamiliaDB.listAll();
    }

    @GET
    @Path("/{idFamilia}")
    @Produces(MediaType.APPLICATION_JSON)
    public FamiliaDB buscaFamiliaPorId(@PathParam("idFamilia") String id) {
        return FamiliaDB.findById(new ObjectId(id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFamilia(FamiliaDB familiaDB) {

        ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);

        try {
            
            familiaDB.constroi().persist();
            builder.status(Response.Status.CREATED);

        } catch (Exception exc) {
            
			builder.status(Response.Status.BAD_REQUEST).entity(exc.getMessage());
        }

        return builder.build();
    }

    @GET
    @Path("/{idFamilia}/membros")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MembroDaFamiliaDB> getMembrosDaFamilia(@PathParam("idFamilia") String idFamilia, @PathParam("idMembro") String idMembro) {
        
        FamiliaDB familiaDB = FamiliaDB.findById(new ObjectId(idFamilia));
        return familiaDB.membros;
    }

    @POST
    @Path("/{idFamilia}/membros")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postMembroDaFamilia(@PathParam("idFamilia") String idFamilia,  MembroDaFamiliaDB membroDaFamiliaDB) {

        ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);

        try {
            
            FamiliaDB familiaDB = FamiliaDB.findById(new ObjectId(idFamilia));
            familiaDB.adicionaMembro(membroDaFamiliaDB).constroi().update();
            builder.status(Response.Status.CREATED);

        } catch (Exception exc) {
            
			builder.status(Response.Status.BAD_REQUEST). entity(exc.getMessage()).type("text/plain");
        }

        return builder.build();
    }

    @GET
    @Path("/{idFamilia}/fundos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FundoFamiliarDB> getFundos(@PathParam("idFamilia") String idFamilia) {
        return FundoFamiliarDB.find("idFamilia = ?1", idFamilia).list();
    }

    @POST
    @Path("/{idFamilia}/fundos")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postFundos(@PathParam("idFamilia") String idFamilia, FundoFamiliarDB fundo) {
        fundo.persist();
        return Response.ok().build();
    }

}
-- Inserindo Paciente   (hierarquia: Pessoa -> PessoaFisica -> Paciente)
INSERT INTO pessoa (tipo, email, telefone, nome, cpf) VALUES ('PACIENTE', 'heloisa@gmail.com', '63999991111', 'Heloisa', '111.111.111-11');
INSERT INTO pessoa (tipo, email, telefone, nome, cpf) VALUES ('PACIENTE', 'tandson@gmail.com', '63988882222', 'Tandson', '222.222.222-22');

-- Inserindo Medico     (hierarquia: Pessoa -> PessoaFisica -> Medico)
INSERT INTO pessoa (tipo, email, telefone, nome, cpf, crm) VALUES ('MEDICO', 'roberto@clinica.com', '63977773333', 'Dr. Roberto Fernandes', '333.333.333-33', 'CRM/TO 1234');
INSERT INTO pessoa (tipo, email, telefone, nome, cpf, crm) VALUES ('MEDICO', 'maria@clinica.com', '63966664444', 'Dra. Maria Oliveira', '444.444.444-44', 'CRM/TO 5678');

-- Inserindo PessoaJuridica (hierarquia: Pessoa -> PessoaJuridica)
INSERT INTO pessoa (tipo, email, telefone, razao_social, cnpj) VALUES ('PJ', 'clinica@clinicasps.com', '63955556666', 'Clinica Sao Paulo SA', '11.111.111/0001-11');
INSERT INTO pessoa (tipo, email, telefone, razao_social, cnpj) VALUES ('PJ', 'hospital@hospitalvida.com', '63944447777', 'Hospital Vida Ltda', '22.222.222/0001-22');

-- Inserindo Consulta
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-03-10 14:30:00', 250.00, 'Consulta de Rotina', 1, 3);
INSERT INTO consulta (data, valor, observacao, paciente_id, medico_id) VALUES ('2026-03-11 10:00:00', 300.00, 'Retorno do paciente', 2, 4);
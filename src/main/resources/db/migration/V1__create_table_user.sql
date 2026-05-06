CREATE TABLE mentes_edition(
    id UUID PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    date_edition DATE NOT NULL,
    zip_code VARCHAR(9) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO mentes_edition (id, title, date_edition, zip_code, city, state)
VALUES
    ('10000000-0000-0000-0000-000000000001', 'Mentes Brilhantes I', '2026-08-20', '25880-000', 'Sapucaia', 'RJ'),
    ('10000000-0000-0000-0000-000000000002', 'Mentes Brilhantes II', '2026-08-20', '25880-000', 'Sapucaia', 'RJ'),
    ('10000000-0000-0000-0000-000000000003', 'Mentes Brilhantes III', '2026-08-20', '25880-000', 'Sapucaia', 'RJ'),
    ('10000000-0000-0000-0000-000000000004', 'Mentes Brilhantes IV', '2026-08-20', '25880-000', 'Sapucaia', 'RJ');


CREATE TABLE users(
    /* DADOS PESSOAIS*/
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    url_image varchar(255),
    instagram varchar(100),
    date_of_birth DATE,
    how_people_live_with_you INTEGER,

    /* DADOS UTEIS NO APP */
    user_type VARCHAR(20) NOT NULL,
    total_points INTEGER,
    educational_instituition varchar(100),
    active boolean DEFAULT FALSE,
    mentes_editions UUID,
    redeemable_points INTEGER DEFAULT 0 NOT NULL,


    /* ENDEREÇO */
    street varchar(255),
    number varchar(10),
    neigborhood varchar(50),
    city varchar(50),
    state varchar(2),
    complement varchar(100),
    zip_code varchar(9),

    /* DADOS AUDITORIA*/
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

CREATE TABLE live_with_you (
                               id UUID PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               relationshipType VARCHAR(50) NOT NULL,
                               user_id UUID NOT NULL,
                               phone VARCHAR(15),

    /* DADOS AUDITORIA*/
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               deleted_at TIMESTAMP,

                               CONSTRAINT fk_live_with_you_user FOREIGN KEY (user_id) REFERENCES users(id)

);

CREATE TABLE partners(
                         id UUID PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         image_url VARCHAR(255),
                         url VARCHAR(255),
                         validity DATE,
                         zip_code VARCHAR(9) NOT NULL,
                         city VARCHAR(50) NOT NULL,
                         state VARCHAR(2) NOT NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         deleted_at TIMESTAMP
);

INSERT INTO partners (id, name, image_url, url, validity, zip_code, city, state)
VALUES
    ('20000000-0000-0000-0000-000000000001', 'AV Sistems', NULL, 'https://avsistems.com.br', '2050-12-31', '25880-000', 'Sapucaia', 'RJ');


CREATE TABLE gifts (
                        id UUID PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        points_cost INTEGER NOT NULL,
    is_advanced BOOLEAN DEFAULT FALSE,
                        stock INTEGER NOT NULL DEFAULT 0,
                        status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                        partner_id UUID,
    requeriments VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        deleted_at TIMESTAMP,
                        CONSTRAINT fk_gifts_partner FOREIGN KEY (partner_id) REFERENCES partners(id)
);

INSERT INTO gifts (id, name, points_cost, is_advanced, stock, status, partner_id, requeriments)
VALUES
    ('30000000-0000-0000-0000-000000000001', 'Quebra cabeça mentes brilhantes 200 peças', 200, FALSE, 50, 'ACTIVE', '20000000-0000-0000-0000-000000000001',''),
    ('30000000-0000-0000-0000-000000000002', 'Ir ao Advanced', 250, FALSE, 50, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000003', 'Garrafinha Mentes Brilhantes', 100, TRUE, 20, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000004', 'Caneta Personalizada', 50, FALSE, 20, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000005', 'Mala/Mochila Mentes Brilhantes', 300, TRUE, 5, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000006', 'Caixinha de som Mentes Brilhantes', 350, TRUE, 10, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000007', 'Necessarie Mentes Brilhantes', 150, TRUE, 10, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000008', 'Ecobag Mentes Brilhantes', 100, FALSE, 100,'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000009', 'Moletom', 200, FALSE, 10, 'ACTIVE', NULL,''),
    ('30000000-0000-0000-0000-000000000010', 'Boné', 60, FALSE, 10, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000011', 'Camisa Experienete', 80, FALSE, 10, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000012', 'Ir no sabado da imersão', 80, FALSE, 100, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000013', 'Participar dos dois dias da imersão', 120, FALSE, 20, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000014', 'Partivipar do 1%', 50, FALSE, 10, 'ACTIVE', NULL, 'ir na imersão e ir na final'),
    ('30000000-0000-0000-0000-000000000015', 'Uma vaga na final do advanced', 450, FALSE, 10, 'ACTIVE', NULL, 'ter a camisa experientes'),
    ('30000000-0000-0000-0000-000000000016', 'Um final de semana em um eco/resort com 1 acompanhante (valido somente para os dois primeiros', 599, FALSE, 2, 'ACTIVE', NULL, ''),
    ('30000000-0000-0000-0000-000000000017', 'Vale compras no comercio parceiro', 600, FALSE, 10, 'ACTIVE', NULL, 'tenha mais de 600 pontos, neste caso, cada ponto vira 1 real em vale compras');

CREATE TABLE gifts_redemptions (
                                   id UUID PRIMARY KEY,
                                   user_id UUID NOT NULL,
                                   gifts_id UUID NOT NULL,
                                   status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                                   points_used INTEGER NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   deleted_at TIMESTAMP,
                                   CONSTRAINT fk_gifts_redemptions_user FOREIGN KEY (user_id) REFERENCES users(id),
                                   CONSTRAINT fk_gifts_redemptions_gifts FOREIGN KEY (gifts_id) REFERENCES gifts(id)
);



CREATE TABLE tasks (
                       id UUID PRIMARY KEY,
                       name varchar(255) NOT NULL,
                       tasks_status varchar(20) DEFAULT 'ACTIVE',
                       tasks_points integer,
                       tasks_type varchar(20),
                       gifts_id UUID,
                       recurrence_days INTEGER DEFAULT 30 NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       deleted_at TIMESTAMP,

                       CONSTRAINT fk_tasks_gifts FOREIGN KEY (gifts_id) REFERENCES gifts(id)
);

CREATE TABLE task_user_completed (
                                     id UUID PRIMARY KEY,
                                     task_id UUID NOT NULL,
                                     user_id UUID NOT NULL,
                                     verified BOOLEAN NOT NULL DEFAULT FALSE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     deleted_at TIMESTAMP,

                                     CONSTRAINT fk_task_user_completed_task FOREIGN KEY (task_id) REFERENCES tasks(id),
                                     CONSTRAINT fk_task_user_completed_user FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO tasks (id, name, tasks_status, tasks_points, tasks_type, gifts_id, recurrence_days)
VALUES
    ('40000000-0000-0000-0000-000000000001', 'Faça alguém feliz', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000002', 'Ação Social', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000003','Apresente o treinamento para um grupo de pessoas(mínimo 6)', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000004', 'Traga 20 novos seguidores para o nosso instagram', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000005', 'Faça um video falando sobre empreendedorismo', 'ACTIVE', 5, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000006', 'Faça um video falando sobre gestão pública', 'ACTIVE', 5, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000007', 'Faça um video motivacional', 'ACTIVE', 5, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000008', 'Poste um video falando bem de um mentes brilhantes, sobre sua evolução pos treinamento com a #MEUAMIGOMB', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000009', 'Poste um depoimento de alguém especial falando sobre as suas mudanças após o treinamento com a #MBMUDA', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000010', 'Poste uma foto/vídeo com a sua família com a #MBFAMILIA', 'ACTIVE', 3, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000011', 'Poste uma foto sua com seu animal de estimação com a #MBANIMAL', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000012', 'Divulgue um empreendedor local(não pode repetir durante 6 meses)', 'ACTIVE', 5, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000013', 'Poste você doando sangue com a #MBSEJADOADOR', 'ACTIVE', 30, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000014', 'Poste um depoimento falando sobre a sua mudança no treinamento', 'ACTIVE', 30, 'NORMAL', NULL, 365),
    ('40000000-0000-0000-0000-000000000015', 'Poste uma foto/vídeo do treinamento com a #TBEXPERIENTES', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000016', 'Poste uma foto/video/frase fasendo o que gosta com a frase #SEXTOUMB', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000017', 'Divulgue o seu negócio ou da sua família que você ajuda com #MENTESEMPREENDEDORAS', 'ACTIVE', 3, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000018', 'Dance suas músicas preferidas com a #MBDANÇA', 'ACTIVE', 10, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000019', 'Encontre um negócio parceiro (pode até 3 por mês)', 'ACTIVE', 10, 'NORMAL', NULL, 1),
    ('40000000-0000-0000-0000-000000000020', 'Tire uma foto na frente de um negocio parceiro com o proprietário', 'ACTIVE', 2, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000021', 'Participe dos eventos dos experientes', 'ACTIVE', 10, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000022', 'Poste um certificado de conclusão de cursos extra curriculares com a #+1', 'ACTIVE', 5, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000023', 'Demonstre sua fé por meio de foto ou vídeo com a #MBTENHAFÉ', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000024', 'Conte-nos sobre um livro que gostou de ler com a #MBLERPARASOBREVIVER', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000025', 'Indique filmes/séries que gostou com a #MBFILMES', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000026', 'Poste uma foto/vídeo usando roupas com as cores de sua equipe coma #MBSTYLE', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000027', 'Faça uma postagem indicando perfis que vocês curtem e que compartilham algo de bom com a #EXPERIENTESINDICAM', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000028', 'Poste um vlog mostrando o seu dia com a #VLOGEXPERIENTE', 'ACTIVE', 5, 'NORMAL', NULL, 30),
    ('40000000-0000-0000-0000-000000000029', 'Montou um pequeno negócio formal', 'ACTIVE', 50, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000030', 'Montou um negócio artesanal', 'ACTIVE', 20, 'NORMAL', NULL, 183),
    ('40000000-0000-0000-0000-000000000031', 'Seja advanced e monte um pequeno negócio formal', 'ACTIVE', 200, 'NORMAL', NULL, 70),
    ('40000000-0000-0000-0000-000000000032', 'Arrume um emprego formal posterior ao seu treinamento', 'ACTIVE', 100, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000033', 'Consiga um estágio remunerado em órgãos públicos', 'ACTIVE', 10, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000034', 'Traga, no mínimo, 3 alunos para o próximo treinamento', 'ACTIVE', 30, 'NORMAL', NULL, 365),
    ('40000000-0000-0000-0000-000000000035', 'Não é advanced mais montou um negócio formal', 'ACTIVE', 30, 'NORMAL', NULL, 0),
    ('40000000-0000-0000-0000-000000000036', 'Poste uma foto/vídeo mostrando que é fera na cozinha coma #MBDEUFOME', 'ACTIVE', 3, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000037', 'Mostre um vídeo da sua arte (pintura, desenho, atuação, música, artesanato...) com a #MBARTE', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000038', 'Poste uma foto/video praticando esportes com a #MBPRATIQUEESPORTES', 'ACTIVE', 1, 'NORMAL', NULL, 7),
    ('40000000-0000-0000-0000-000000000039', 'Poste um video plantando uma árvore ou uma planta', 'ACTIVE', 10, 'NORMAL', NULL, 30);

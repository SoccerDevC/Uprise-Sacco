<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateContributionsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('contributions', function (Blueprint $table) {
            $table->integer('receipt_number')->primary();
            $table->string('member_id');
            $table->decimal('contribution_amount', 10, 2);
            $table->date('contribution_date');
            $table->decimal('maximum_contribution', 10, 2)->nullable();
            $table->timestamps();

            $table->foreign('member_id')->references('member_id')->on('members');
        });

        Schema::table('contributions', function (Blueprint $table) {
            // Calculate the maximum contribution by summing the previous contributions
            $table->decimal('previous_contributions', 10, 2)->default(0)->after('contribution_amount');
            $table->decimal('total_contribution', 10, 2)->virtualAs('previous_contributions + contribution_amount')->after('previous_contributions');
        });
    }

    public function down()
    {
        Schema::dropIfExists('contributions');
    }
}

